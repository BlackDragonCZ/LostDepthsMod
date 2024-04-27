package cz.blackdragoncz.lostdepths.client.jade;

import cz.blackdragoncz.lostdepths.block.entity.AbstractCompressorBlockEntity;
import cz.blackdragoncz.lostdepths.block.base.AbstractCompressorBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeComponentProvider.INSTANCE, AbstractCompressorBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeComponentProvider.INSTANCE, AbstractCompressorBlock.class);
    }
}
