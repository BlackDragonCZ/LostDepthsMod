
package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.PrimeDraconicPickaxeRightclickedProcedure;
import cz.blackdragoncz.lostdepths.procedures.PrimeDraconicHammerBlockDestroyedWithToolProcedure;
import cz.blackdragoncz.lostdepths.procedures.CrystalizedPickaxeMakeItemGlowProcedure;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class PrimeDraconicHammerItem extends PickaxeItem {
	public PrimeDraconicHammerItem() {
		super(new Tier() {
			public int getUses() {
				return 1024;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 16f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 15;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(LostdepthsModItems.CONDENSED_MALICIUM.get()));
			}
		}, 1, -2.6f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
		boolean retval = super.mineBlock(itemstack, world, blockstate, pos, entity);
		PrimeDraconicHammerBlockDestroyedWithToolProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
		return retval;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		PrimeDraconicPickaxeRightclickedProcedure.execute(entity);
		return ar;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("§6Right Click: Activate Speed and Regeneration."));
		list.add(Component.literal("§5Can Mine area of 3x3."));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		PrimeDraconicPickaxeRightclickedProcedure.execute(context.getPlayer());
		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return CrystalizedPickaxeMakeItemGlowProcedure.execute();
	}
}
