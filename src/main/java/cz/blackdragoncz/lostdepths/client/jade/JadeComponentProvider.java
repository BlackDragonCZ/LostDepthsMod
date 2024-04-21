package cz.blackdragoncz.lostdepths.client.jade;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.AbstractCompressorBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum JadeComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getServerData().contains("time")) {
            IElementHelper elements = tooltip.getElementHelper();
            IElement icon = elements.item(new ItemStack(Items.CLOCK), 0.5f).size(new Vec2(10,10)).translate(new Vec2(0,-1));
            icon.message(null);
            tooltip.add(icon);
            tooltip.append(Component.translatable("lostdepths.time", accessor.getServerData().getInt("time")));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        AbstractCompressorBlockEntity compressor = (AbstractCompressorBlockEntity) accessor.getBlockEntity();
        if (compressor.getCurrentCraftTime() > 0) {
            data.putInt("time", (compressor.getCraftTickTime() - compressor.getCurrentCraftTime()) / 20);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return LostdepthsMod.rl("time");
    }

}
