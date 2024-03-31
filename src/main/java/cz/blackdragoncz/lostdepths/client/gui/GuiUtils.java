package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GuiUtils {

    public static void renderItem(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis, float scale, Font font, @Nullable String text, boolean overlay) {
        if (!stack.isEmpty()) {
            try {
                PoseStack pose = guiGraphics.pose();
                pose.pushPose();
                if (scale != 1) {
                    //Translate before scaling, and then set xAxis and yAxis to zero so that we don't translate a second time
                    pose.translate(xAxis, yAxis, 0);
                    pose.scale(scale, scale, scale);
                    xAxis = 0;
                    yAxis = 0;
                }
                guiGraphics.renderItem(stack, xAxis, yAxis);
                if (overlay) {
                    //When we render items ourselves in virtual slots or scroll slots we want to compress the z scale
                    // for rendering the stored count so that it doesn't clip with later windows
                    pose.translate(0, 0, -25);
                    guiGraphics.renderItemDecorations(font, stack, xAxis, yAxis, text);
                }

                pose.popPose();
            } catch (Exception e) {
                LogUtils.getLogger().error("Failed to render stack into gui: {}", stack, e);
            }
        }
    }

}
