package us.devs.ingrosware.gui.click.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.gui.click.component.Component;
import us.devs.ingrosware.setting.impl.BooleanSetting;
import us.devs.ingrosware.setting.impl.NumberSetting;
import us.devs.ingrosware.util.math.MathUtil;
import us.devs.ingrosware.util.math.MouseUtil;
import us.devs.ingrosware.util.render.RenderUtil;

import java.awt.*;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/18/2020
 **/
public class NumberComponent extends Component {
    private final NumberSetting numberSetting;
    private boolean dragging;
    public NumberComponent(NumberSetting numberSetting, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberSetting.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float sliderwidth = 74;
        float length = MathHelper.floor(((float) getNumberSetting().getValue() - getNumberSetting().getMinimum().floatValue()) / (getNumberSetting().getMaximum().floatValue() - getNumberSetting().getMinimum().floatValue()) * sliderwidth);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 80, getHeight(), 0.5F, 0xff353535,0xff000000);
        RenderUtil.drawRect(getPosX() + length + 1f, getPosY() + 1, 4f, getHeight() - 2, 0xff505050);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getNumberSetting().getLabel() + ": " + getNumberSetting().getValue().toString(), getPosX() + 40 - (Minecraft.getMinecraft().fontRenderer.getStringWidth(getNumberSetting().getLabel() + ": " + getNumberSetting().getValue().toString()) >> 1), getPosY() + 4, -1);
        if (isDragging()) {
            if (getNumberSetting().getValue() instanceof Double) {
                getNumberSetting().setValue(MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().doubleValue() - getNumberSetting().getMinimum().doubleValue()) / sliderwidth + getNumberSetting().getMinimum().doubleValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Float) {
                getNumberSetting().setValue((float) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().floatValue() - getNumberSetting().getMinimum().floatValue()) / sliderwidth + getNumberSetting().getMinimum().floatValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Long) {
                getNumberSetting().setValue((long) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().longValue() - getNumberSetting().getMinimum().longValue()) / sliderwidth + getNumberSetting().getMinimum().longValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Integer) {
                getNumberSetting().setValue((int) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().intValue() - getNumberSetting().getMinimum().intValue()) / sliderwidth + getNumberSetting().getMinimum().intValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Short) {
                getNumberSetting().setValue((short) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().shortValue() - getNumberSetting().getMinimum().shortValue()) / sliderwidth + getNumberSetting().getMinimum().shortValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Byte) {
                getNumberSetting().setValue((byte) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().byteValue() - getNumberSetting().getMinimum().byteValue()) / sliderwidth + getNumberSetting().getMinimum().byteValue()), 2));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 80, 14);
        if (isHovered)
            setDragging(true);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isDragging())
            setDragging(false);
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public NumberSetting getNumberSetting() {
        return numberSetting;
    }
}
