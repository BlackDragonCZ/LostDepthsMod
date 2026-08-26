package cz.blackdragoncz.lostdepths.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class LostdepthsModWoodTypes {

    // Same as BlockSetType.IRON but canOpenByHand=true, which IRON hardcodes to false.
    public static final BlockSetType INFUSED_IRON_SET = BlockSetType.register(new BlockSetType(
            "lostdepths:infused_iron", true, SoundType.METAL,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    // Stays on IRON: drives the signs, changing it would alter their sounds.
    public static WoodType INFUSED_IRON = WoodType.register(new WoodType("lostdepths:infused_iron", BlockSetType.IRON));
}
