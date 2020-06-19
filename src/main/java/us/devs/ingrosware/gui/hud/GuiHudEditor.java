package us.devs.ingrosware.gui.hud;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.gui.hud.settings.HudSettings;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.type.CustomComponent;
import us.devs.ingrosware.util.math.MouseUtil;
import us.devs.ingrosware.util.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class GuiHudEditor extends GuiScreen {

    @Override
    public void drawScreen(int mx, int my, float p_drawScreen_3_) {
        super.drawScreen(mx, my, p_drawScreen_3_);
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        IngrosWare.INSTANCE.getComponentManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isDragging()) {
                hudComponent.setX(mx + hudComponent.getLastX());
                hudComponent.setY(my + hudComponent.getLastY());
            }
            if (hudComponent.getX() < 0) {
                hudComponent.setX(0);
            }
            if (hudComponent.getX() + hudComponent.getWidth() > scaledResolution.getScaledWidth()) {
                hudComponent.setX(scaledResolution.getScaledWidth() - hudComponent.getWidth());
            }
            if (hudComponent.getY() < 0) {
                hudComponent.setY(0);
            }
            if (hudComponent.getY() + hudComponent.getHeight() > scaledResolution.getScaledHeight()) {
                hudComponent.setY(scaledResolution.getScaledHeight() - hudComponent.getHeight());
            }
            if (!hudComponent.isHidden()) hudComponent.onDraw(scaledResolution);

            RenderUtil.drawRect(hudComponent.getX(), hudComponent.getY(), hudComponent.getWidth(), hudComponent.getHeight(), hudComponent.isDragging() ? 0x95000000 : 0x80000000);

            if (!hudComponent.isLabelHidden())
                fontRenderer.drawStringWithShadow(hudComponent.getLabel(), hudComponent.getX() + hudComponent.getWidth() / 2 - (RenderUtil.getStringWidth(hudComponent.getLabel()) / 2), hudComponent.getY() + hudComponent.getHeight() / 2 - (RenderUtil.getStringHeight() / 2), new Color(255, 255, 255, 83).getRGB());
        });
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }

    @Override
    protected void mouseClicked(int mx, int my, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(mx, my, p_mouseClicked_3_);
        IngrosWare.INSTANCE.getComponentManager().getValues().forEach(hudComponent -> {
            final boolean hovered = MouseUtil.mouseWithin(mx, my, hudComponent.getX(), hudComponent.getY(), hudComponent.getWidth(), hudComponent.getHeight());
            switch (p_mouseClicked_3_) {
                case 0:
                    if (hovered) {
                        hudComponent.setDragging(true);
                        hudComponent.setLastX(hudComponent.getX() - mx);
                        hudComponent.setLastY(hudComponent.getY() - my);
                    }
                    break;
                case 1:
                    if (hovered) {
                        mc.displayGuiScreen(new HudSettings(hudComponent));
                    } else {
                        mc.displayGuiScreen(new CustomHudComponent());
                    }
                    break;
                case 2:
                    if (hovered)
                        hudComponent.setHidden(!hudComponent.isHidden());
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void mouseReleased(int mx, int my, int p_mouseReleased_3_) {
        super.mouseReleased(mx, my, p_mouseReleased_3_);
        IngrosWare.INSTANCE.getComponentManager().getValues().forEach(hudComponent -> {
            if (p_mouseReleased_3_ == 0 && hudComponent.isDragging())
                hudComponent.setDragging(false);
        });
    }

    @Override
    public void onGuiClosed() {
        IngrosWare.INSTANCE.getComponentManager().getValues().forEach(hudComponent -> {
            if (hudComponent.isDragging())
                hudComponent.setDragging(false);
        });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
