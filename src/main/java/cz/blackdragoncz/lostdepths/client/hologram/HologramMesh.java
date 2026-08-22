package cz.blackdragoncz.lostdepths.client.hologram;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;

// A structure NBT reduced to one coloured cube per block, with every face that touches another block dropped. Built once per template on resource
// reload; the renderer only walks the surviving faces. A solid 64-cube goes from ~1.5M faces to ~24k this way.
public final class HologramMesh {

	public static final HologramMesh EMPTY = new HologramMesh(0, 0, 0, new int[0], new int[0], new byte[0], new int[0], new int[0]);

	private final int sizeX;
	private final int sizeY;
	private final int sizeZ;
	private final int[] faceX;
	private final int[] faceY;
	private final byte[] faceDir;
	private final int[] faceColor;
	private final int[] faceZ;

	private HologramMesh(int sizeX, int sizeY, int sizeZ, int[] faceX, int[] faceY, byte[] faceDir, int[] faceColor, int[] faceZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.faceX = faceX;
		this.faceY = faceY;
		this.faceDir = faceDir;
		this.faceColor = faceColor;
		this.faceZ = faceZ;
	}

	public int sizeX() {
		return sizeX;
	}

	public int sizeY() {
		return sizeY;
	}

	public int sizeZ() {
		return sizeZ;
	}

	public int faceCount() {
		return faceDir.length;
	}

	public int x(int i) {
		return faceX[i];
	}

	public int y(int i) {
		return faceY[i];
	}

	public int z(int i) {
		return faceZ[i];
	}

	public Direction direction(int i) {
		return Direction.from3DDataValue(faceDir[i]);
	}

	public int color(int i) {
		return faceColor[i];
	}

	public static HologramMesh bake(CompoundTag nbt) {
		ListTag sizeTag = nbt.getList("size", Tag.TAG_INT);
		if (sizeTag.size() != 3)
			return EMPTY;
		int sx = sizeTag.getInt(0);
		int sy = sizeTag.getInt(1);
		int sz = sizeTag.getInt(2);
		if (sx <= 0 || sy <= 0 || sz <= 0)
			return EMPTY;

		int[] palette = readPalette(nbt);
		// 0 means empty. Colours keep a set alpha bit so a genuinely black block is still distinguishable from air.
		int[] grid = new int[sx * sy * sz];
		ListTag blocks = nbt.getList("blocks", Tag.TAG_COMPOUND);
		for (int i = 0; i < blocks.size(); i++) {
			CompoundTag entry = blocks.getCompound(i);
			ListTag pos = entry.getList("pos", Tag.TAG_INT);
			if (pos.size() != 3)
				continue;
			int x = pos.getInt(0);
			int y = pos.getInt(1);
			int z = pos.getInt(2);
			if (x < 0 || y < 0 || z < 0 || x >= sx || y >= sy || z >= sz)
				continue;
			int state = entry.getInt("state");
			int color = state >= 0 && state < palette.length ? palette[state] : 0;
			if (color != 0)
				grid[index(x, y, z, sx, sz)] = color;
		}

		return cull(grid, sx, sy, sz);
	}

	private static HologramMesh cull(int[] grid, int sx, int sy, int sz) {
		int capacity = 64;
		int[] fx = new int[capacity];
		int[] fy = new int[capacity];
		int[] fz = new int[capacity];
		byte[] fd = new byte[capacity];
		int[] fc = new int[capacity];
		int n = 0;

		for (int y = 0; y < sy; y++) {
			for (int z = 0; z < sz; z++) {
				for (int x = 0; x < sx; x++) {
					int color = grid[index(x, y, z, sx, sz)];
					if (color == 0)
						continue;
					for (Direction dir : Direction.values()) {
						int nx = x + dir.getStepX();
						int ny = y + dir.getStepY();
						int nz = z + dir.getStepZ();
						boolean outside = nx < 0 || ny < 0 || nz < 0 || nx >= sx || ny >= sy || nz >= sz;
						if (!outside && grid[index(nx, ny, nz, sx, sz)] != 0)
							continue;
						if (n == fd.length) {
							int grown = n * 2;
							fx = java.util.Arrays.copyOf(fx, grown);
							fy = java.util.Arrays.copyOf(fy, grown);
							fz = java.util.Arrays.copyOf(fz, grown);
							fd = java.util.Arrays.copyOf(fd, grown);
							fc = java.util.Arrays.copyOf(fc, grown);
						}
						fx[n] = x;
						fy[n] = y;
						fz[n] = z;
						fd[n] = (byte) dir.get3DDataValue();
						fc[n] = color;
						n++;
					}
				}
			}
		}

		return new HologramMesh(sx, sy, sz, java.util.Arrays.copyOf(fx, n), java.util.Arrays.copyOf(fy, n), java.util.Arrays.copyOf(fd, n), java.util.Arrays.copyOf(fc, n), java.util.Arrays.copyOf(fz, n));
	}

	private static int[] readPalette(CompoundTag nbt) {
		ListTag paletteTag = nbt.getList("palette", Tag.TAG_COMPOUND);
		// Structures saved with random variants store "palettes" instead; the first one is representative enough for a hologram.
		if (paletteTag.isEmpty()) {
			ListTag palettes = nbt.getList("palettes", Tag.TAG_LIST);
			if (!palettes.isEmpty() && palettes.get(0) instanceof ListTag first)
				paletteTag = first;
		}
		int[] colors = new int[paletteTag.size()];
		for (int i = 0; i < paletteTag.size(); i++) {
			BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), paletteTag.getCompound(i));
			if (state.isAir())
				continue;
			int rgb = state.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).col;
			colors[i] = rgb == 0 ? 0xFF000001 : 0xFF000000 | rgb;
		}
		return colors;
	}

	// Layer-major: a whole XZ slab per Y step.
	private static int index(int x, int y, int z, int sx, int sz) {
		return (y * sx * sz) + (z * sx) + x;
	}
}
