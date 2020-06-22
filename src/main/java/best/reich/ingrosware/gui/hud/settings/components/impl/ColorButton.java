package best.reich.ingrosware.gui.hud.settings.components.impl;

import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.hud.settings.components.HudSetting;

import java.awt.*;

/**
 * made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
public class ColorButton extends HudSetting {
    private final ColorSetting colorSetting;
    private boolean pressedHue;
    private float pos, hue, saturation, brightness;
    private boolean hovered;

    public ColorButton(ColorSetting colorSetting, float posX, float posY) {
        super(colorSetting.getLabel(), posX, posY);
        this.colorSetting = colorSetting;
        float[] hsb = new float[3];
        final Color clr = colorSetting.getValue();
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.pos = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Keyboard.enableRepeatEvents(true);
        hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY() + 12, 100,12);
        for (float i = 0; i + 1 < 100; i += 0.5f) {
            float x = getPosX() + i;
            int color = Color.getHSBColor(i / 100, saturation, brightness).getRGB();
            RenderUtil.drawRect(x, getPosY() + 12, 1, 12, color);
            if (mouseX == x) {
                if (pressedHue) {
                    colorSetting.setValue(color);
                    hue = i / 100;
                }
            }
            if (0.001 * Math.floor((i / 100) * 1000.0) == 0.001 * Math.floor(hue * 1000.0)) pos = i;
        }
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(), getPosX(),getPosY() + 1, 0xFFFFFFFF);
        RenderUtil.drawRect(getPosX() + pos, getPosY() + 13, 1, 10, 0xffffffff);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
        if (!hovered) return;
        switch (keyCode) {
            case Keyboard.KEY_UP:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness + 0.01 <= 1) brightness += 0.01;
                } else {
                    if (saturation + 0.01 <= 1) saturation += 0.01;
                }
                colorSetting.setValue(Color.HSBtoRGB(hue, saturation, brightness));
                break;
            case Keyboard.KEY_DOWN:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness - 0.01 >= 0) brightness -= 0.01;
                } else {
                    if (saturation - 0.01 >= 0) saturation -= 0.01;
                }
                colorSetting.setValue(Color.HSBtoRGB(hue, saturation, brightness));
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY() + 12, 100,12);
        if (mouseButton == 0) {
            if (hovered) {
                pressedHue = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (pressedHue) {
                pressedHue = false;
            }
        }
    }
}