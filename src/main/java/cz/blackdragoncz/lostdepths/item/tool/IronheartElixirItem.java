
package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.IronheartElixirRightclickedOnBlockProcedure;

public class IronheartElixirItem extends Item {
	public IronheartElixirItem() {
		super(new Item.Properties().durability(0).fireResistant());
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState blockstate) {
		return 2f;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		IronheartElixirRightclickedOnBlockProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity);
		return ar;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("§6When drunk, grants the Ironheart buff"));
		list.add(Component.literal("§4Ironheart grants immunity to max health damage."));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		IronheartElixirRightclickedOnBlockProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getPlayer());
		return InteractionResult.SUCCESS;
	}
}
