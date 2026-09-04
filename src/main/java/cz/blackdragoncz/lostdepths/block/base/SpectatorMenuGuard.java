package cz.blackdragoncz.lostdepths.block.base;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

// Spectators never reach Block.use(): ServerPlayerGameMode:331 short-circuits to getMenuProvider + openMenu, which sends the plain
// vanilla open packet carrying no BlockPos. Our menus read their position out of the Forge extra-data buffer, so the client builds one
// with a null block entity and the screen NPEs on its first frame. RightClickBlock fires at :329, one line earlier, so cancel there.
@Mod.EventBusSubscriber
public final class SpectatorMenuGuard {

    private SpectatorMenuGuard() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().isSpectator())
            return;
        BlockEntity be = event.getLevel().getBlockEntity(event.getPos());
        if (!(be instanceof MenuProvider))
            return;
        // Only our own blocks: another mod's menu may well survive being opened this way.
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(event.getLevel().getBlockState(event.getPos()).getBlock());
        if (key != null && LostdepthsMod.MODID.equals(key.getNamespace()))
            event.setCanceled(true);
    }
}
