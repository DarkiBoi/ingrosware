package us.devs.ingrosware.hud.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.gui.hud.CustomHudComponent;
import us.devs.ingrosware.gui.hud.settings.HudSettings;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.hud.type.ClickableComponent;
import us.devs.ingrosware.util.math.MouseUtil;
import us.devs.ingrosware.util.render.RenderUtil;

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

        int offsetY = (int) (getY()) + 17;
        for(Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if(component != null) {
                if(!(component instanceof ClickableComponent)) {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    RenderUtil.prepareScissorBox(scaledResolution, getX(), getY() + 15, getWidth(), getHeight());

                    RenderUtil.drawBorderedRect(getX(), offsetY, getWidth(),
                            getHeight() - 185, 0.5F, new Color(0x1E1E1E).getRGB(), new Color(0x1E1E1E).getAlpha());

                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(component.getLabel(),
                            getX() + 5, offsetY + 5, -1);
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
        int offsetY = (int) (getY()) + 17;
        for(Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if(component != null) {
                if(!(component instanceof ClickableComponent)) {
                    boolean isOver = MouseUtil.mouseWithin(mx, my, getX(), offsetY, getWidth(), getHeight() - 185);

                    if(isOver) {
                        switch (p_mouseClicked_3_) {
                            case 0:
                                component.setHidden(!component.isHidden());
                                break;
                            case 1:
                                mc.displayGuiScreen(new HudSettings(component));
                                break;
                        }
                    }

                    offsetY += 16;
                }
            }
        }
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }

    private void handleScrolling(int mouseX, int mouseY) {
        if (MouseUtil.mouseWithin(mouseX, mouseY, getX() + 6.0f, getY() + 40, getWidth(), getHeight() - 50) && getHeight() > (getHeight() - 50)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (getScrollY() - 6 < -(getHeight() - Math.min(getHeight(), (getHeight() - 50))))
                    setScrollY((int) -(getHeight() - Math.min(getHeight(), (getHeight() - 50))));
                else setScrollY(getScrollY() - 6);
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 6);
            }
        }
        if (getScrollY() > 0) setScrollY(0);
        if (getHeight() > (getHeight() - 50)) {
            if (getScrollY() - 6 < -(getHeight() - (getHeight() - 50)))
                setScrollY((int) -(getHeight() - (getHeight() - 50)));
        } else if (getScrollY() < 0) setScrollY(0);
    }
}
