package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.click.component.Component;

import java.awt.*;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/18/2020
 **/
public class ColorComponent extends Component {
    private final ColorSetting colorSetting;
    private boolean pressedHue;
    private float pos, hue, saturation, brightness;
    private boolean hovered;

    public ColorComponent(ColorSetting colorSetting, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(colorSetting.getLabel(), posX, posY, offsetX, offsetY, width, height);
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
        setHovered(MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 100, getHeight()));
        for (float i = 0; i + 1 < 100; i += 0.5f) {
            float x = getPosX() + i;
            int color = Color.getHSBColor(i / 100, getSaturation(), getBrightness()).getRGB();
            RenderUtil.drawRect(x, getPosY(), 1, getHeight(), color);
            if (mouseX == x) {
                if (isPressedHue()) {
                    getColorSetting().setValue(color);
                    setHue( i / 100);
                }
            }
            if (0.001 * Math.floor((i / 100) * 1000.0) == 0.001 * Math.floor(getHue() * 1000.0)) setPos(i);
        }
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(),getPosX() + 102,getPosY() + getHeight() - RenderUtil.getStringHeight(),-1);
        RenderUtil.drawBorderedRect(getPosX() + getPos() + 1, getPosY() + 1, 1.5f, getHeight() - 2,0.5f, -1,0xff000000);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 100, getHeight(), 0.5F, 0x00000000,0xff000000);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
        if (!isHovered()) return;
        switch (keyCode) {
            case Keyboard.KEY_UP:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (getBrightness() + 0.01 <= 1) setBrightness(getBrightness() + 0.01f);
                } else {
                    if (getSaturation() + 0.01 <= 1) setSaturation(getSaturation() + 0.01f);
                }
                getColorSetting().setValue(Color.HSBtoRGB(getHue(), getSaturation(), getBrightness()));
                break;
            case Keyboard.KEY_DOWN:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (getBrightness() - 0.01 >= 0) setBrightness(getBrightness() - 0.01f);
                } else {
                    if (getSaturation() - 0.01 >= 0) setSaturation(getSaturation() - 0.01f);
                }
                getColorSetting().setValue(Color.HSBtoRGB(getHue(), getSaturation(), getBrightness()));
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 100, getHeight());
        if (mouseButton == 0) {
            if (hovered) {
                setPressedHue(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (isPressedHue()) {
                setPressedHue(false);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        setPressedHue(false);
    }

    public boolean isPressedHue() {
        return pressedHue;
    }

    public void setPressedHue(boolean pressedHue) {
        this.pressedHue = pressedHue;
    }

    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public ColorSetting getColorSetting() {
        return colorSetting;
    }
}
