package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.gui.hud.settings.HudSettings;
import best.reich.ingrosware.hud.Component;
import best.reich.ingrosware.hud.annotation.ComponentManifest;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import best.reich.ingrosware.hud.type.ClickableComponent;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/19/2020
 **/
@ComponentManifest(label = "Hub", x = 50, y = 10, width = 300, height = 200, hidden = true)
public class HubComponent extends ClickableComponent {
    private float scrollY;

    @Override
    public void onDraw(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.onDraw(mouseX, mouseY, scaledResolution);
        RenderUtil.drawBorderedRect(getX(), getY(), getWidth(), getHeight(), 1, new Color(0x2C2C2C).getRGB(), new Color(0x000000).getAlpha());

        RenderUtil.drawBorderedRect(getX(), getY(), getWidth(), getHeight() - 185, 0.5F, new Color(0x1E1E1E).getRGB(), new Color(0x1E1E1E).getAlpha());

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Hub",
                getX() + 5, getY() + 5, -1);

        this.handleScrolling(mouseX, mouseY);

        int offsetY = (int) ((int) (getY()) + 16 + getScrollY());
        for (best.reich.ingrosware.hud.Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if (component != null) {
                if (!(component instanceof ClickableComponent)) {
                    final boolean hasSettings = IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(component) != null;

                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    RenderUtil.prepareScissorBox(scaledResolution, getX(), getY() + 15, getWidth(), getHeight() - 15);
                    RenderUtil.drawBorderedRect(getX(), offsetY, getWidth(),
                            getHeight() - 185, 0.5F, !component.isLabelHidden() ? 0xff353535 : 0xff505050, 0xff000000);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(component.getLabel() + (hasSettings ? ChatFormatting.WHITE + " +" : ""),
                            getX() + 5, offsetY + 5, component.isHidden() ?
                                    new Color(0xAE3A43).getRGB() : new Color(0x74C472).getRGB());
                    offsetY += 16;
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    @Override
    public void onClick(int mx, int my, int p_mouseClicked_3_) {
        super.onClick(mx, my, p_mouseClicked_3_);
        int offsetY = (int) (getY()) + 16;
        for (best.reich.ingrosware.hud.Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if (component != null) {
                if (!(component instanceof ClickableComponent)) {
                    boolean isOver = MouseUtil.mouseWithin(mx, my, getX(), offsetY, getWidth(), 15);

                    if (isOver) {
                        switch (p_mouseClicked_3_) {
                            case 0:
                                component.setHidden(!component.isHidden());
                                break;
                            case 1:
                                if(IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(component) != null) {
                                    mc.displayGuiScreen(new HudSettings(component));
                                }
                                break;
                        }
                    }

                    offsetY += 16;
                }
            }
        }
    }

    private float getCurrentHeight() {
        float cHeight = 0;
        for (Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if (!(component instanceof ClickableComponent)) {
                cHeight += 16;
            }
        }
        return cHeight;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }

    private void handleScrolling(int mouseX, int mouseY) {
        if (MouseUtil.mouseWithin(mouseX, mouseY, getX(), getY() + 16, getWidth(), getHeight() - 16) && getCurrentHeight() > (getHeight() - 16)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (getScrollY() - 6 < -(getCurrentHeight() - Math.min(getCurrentHeight(), (getHeight() - 16))))
                    setScrollY((int) -(getCurrentHeight() - Math.min(getCurrentHeight(), (getHeight() - 16))));
                else setScrollY(getScrollY() - 6);
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 6);
            }
        }
        if (getScrollY() > 0) setScrollY(0);
        if (getCurrentHeight() > (getHeight() - 16)) {
            if (getScrollY() - 6 < -(getCurrentHeight() - (getHeight() - 16)))
                setScrollY((int) -(getCurrentHeight() - (getHeight() - 16)));
        } else if (getScrollY() < 0) setScrollY(0);
    }
}
