package best.reich.ingrosware.gui.hud.settings.components.impl;


import best.reich.ingrosware.setting.impl.NumberSetting;
import best.reich.ingrosware.util.math.MathUtil;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import best.reich.ingrosware.gui.hud.settings.components.HudSetting;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class NumberButton extends HudSetting {
    private final NumberSetting<Number> numberSetting;
    private boolean dragging;

    public NumberButton(NumberSetting<Number> numberSetting, float posX, float posY) {
        super(numberSetting.getLabel(), posX, posY);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float sliderwidth = 74;
        float length = MathHelper.floor((numberSetting.getValue().floatValue() - numberSetting.getMinimum().floatValue()) / (numberSetting.getMaximum().floatValue() - numberSetting.getMinimum().floatValue()) * sliderwidth);
        boolean isHovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 80, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 80, 10, 0.5F, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());
        RenderUtil.drawRect(getPosX() + length + 1f, getPosY() + 1, 4f, 8, new Color(0, 107, 214, 255).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(numberSetting.getLabel() + ": " + numberSetting.getValue().toString(), getPosX() + 40 -  RenderUtil.getStringWidth(numberSetting.getLabel() + ": " + numberSetting.getValue().toString()) / 2, getPosY() + 2, -1);

        if (dragging) {
            if (numberSetting.getValue() instanceof Double) {
                numberSetting.setValue(MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().doubleValue() - numberSetting.getMinimum().doubleValue()) / sliderwidth + numberSetting.getMinimum().doubleValue()), 2));
            }
            if (numberSetting.getValue() instanceof Float) {
                numberSetting.setValue((float) MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().floatValue() - numberSetting.getMinimum().floatValue()) / sliderwidth + numberSetting.getMinimum().floatValue()), 2));
            }
            if (numberSetting.getValue() instanceof Long) {
                numberSetting.setValue((long) MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().longValue() - numberSetting.getMinimum().longValue()) / sliderwidth + numberSetting.getMinimum().longValue()), 2));
            }
            if (numberSetting.getValue() instanceof Integer) {
                numberSetting.setValue((int) MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().intValue() - numberSetting.getMinimum().intValue()) / sliderwidth + numberSetting.getMinimum().intValue()), 2));
            }
            if (numberSetting.getValue() instanceof Short) {
                numberSetting.setValue((short) MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().shortValue() - numberSetting.getMinimum().shortValue()) / sliderwidth + numberSetting.getMinimum().shortValue()), 2));
            }
            if (numberSetting.getValue() instanceof Byte) {
                numberSetting.setValue((byte) MathUtil.round(((mouseX - getPosX()) * (numberSetting.getMaximum().byteValue() - numberSetting.getMinimum().byteValue()) / sliderwidth + numberSetting.getMinimum().byteValue()), 2));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 100, 10);
        if (isHovered)
            dragging = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (dragging)
            dragging = false;
    }

}
