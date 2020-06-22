package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.setting.impl.NumberSetting;
import best.reich.ingrosware.util.math.MathUtil;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import best.reich.ingrosware.gui.click.component.Component;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/18/2020
 **/
public class NumberComponent extends Component {
    private final NumberSetting<Number> numberSetting;
    private boolean dragging;
    public NumberComponent(NumberSetting<Number> numberSetting, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberSetting.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float sliderWidth = 94;
        float length = MathHelper.floor(((getNumberSetting().getValue()).floatValue() - getNumberSetting().getMinimum().floatValue()) / (getNumberSetting().getMaximum().floatValue() - getNumberSetting().getMinimum().floatValue()) * sliderWidth);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 100, getHeight(), 0.5F, 0xff353535,0xff000000);
        RenderUtil.drawRect(getPosX() + length + 1f, getPosY() + 1, 4f, getHeight() - 2, 0xff505050);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getNumberSetting().getLabel() + ": " + getNumberSetting().getValue().toString(), getPosX() + 102, getPosY() + getHeight() - 1 - RenderUtil.getStringHeight(), -1);
        if (isDragging()) {
            if (getNumberSetting().getValue() instanceof Double) {
                getNumberSetting().setValue(MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().doubleValue() - getNumberSetting().getMinimum().doubleValue()) / sliderWidth + getNumberSetting().getMinimum().doubleValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Float) {
                getNumberSetting().setValue((float) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().floatValue() - getNumberSetting().getMinimum().floatValue()) / sliderWidth + getNumberSetting().getMinimum().floatValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Long) {
                getNumberSetting().setValue((long) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().longValue() - getNumberSetting().getMinimum().longValue()) / sliderWidth + getNumberSetting().getMinimum().longValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Integer) {
                getNumberSetting().setValue((int) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().intValue() - getNumberSetting().getMinimum().intValue()) / sliderWidth + getNumberSetting().getMinimum().intValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Short) {
                getNumberSetting().setValue((short) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().shortValue() - getNumberSetting().getMinimum().shortValue()) / sliderWidth + getNumberSetting().getMinimum().shortValue()), 2));
            }
            if (getNumberSetting().getValue() instanceof Byte) {
                getNumberSetting().setValue((byte) MathUtil.round(((mouseX - getPosX()) * (getNumberSetting().getMaximum().byteValue() - getNumberSetting().getMinimum().byteValue()) / sliderWidth + getNumberSetting().getMinimum().byteValue()), 2));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 100, 14);
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
    public void onGuiClosed() {
        super.onGuiClosed();
        setDragging(false);
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public NumberSetting<Number> getNumberSetting() {
        return numberSetting;
    }
}
