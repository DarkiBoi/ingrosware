package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.setting.impl.ModeStringSetting;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import best.reich.ingrosware.gui.click.component.Component;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/18/2020
 **/
public class ModeStringComponent extends Component {
    private final ModeStringSetting modeStringSetting;
    private boolean extended;
    private final float originalHeight;

    public ModeStringComponent(ModeStringSetting modeStringSetting, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(modeStringSetting.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.modeStringSetting = modeStringSetting;
        this.originalHeight = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 100, getOriginalHeight(), 0.5f, 0xff353535, 0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(StringUtils.capitalize(getModeStringSetting().getValue().toLowerCase()), getPosX() + 50 - (RenderUtil.getStringWidth(StringUtils.capitalize(getModeStringSetting().getValue().toLowerCase())) / 2), getPosY() + getOriginalHeight() - 1 - RenderUtil.getStringHeight(), -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getModeStringSetting().getLabel(), getPosX() + 102, getPosY() + getOriginalHeight() - 1 - RenderUtil.getStringHeight(), -1);
        if (isExtended()) {
            RenderUtil.drawBorderedRect(getPosX() + 5, getPosY() + getOriginalHeight(), 90, getOriginalHeight() * (getModeStringSetting().getModes().length - 1), 0.5f, 0xff353535, 0xff000000);
            float offset = getOriginalHeight() + 4;
            for (String str : getModeStringSetting().getModes()) {
                if (str.equalsIgnoreCase(getModeStringSetting().getValue())) continue;
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(StringUtils.capitalize(str.toLowerCase()), getPosX() + 50 - (RenderUtil.getStringWidth(StringUtils.capitalize(str.toLowerCase())) / 2), getPosY() + offset, 0xff858585);
                offset += getOriginalHeight();
            }
            setHeight(2 + getOriginalHeight() * getModeStringSetting().getModes().length);
        } else setHeight(getOriginalHeight());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), 100, getOriginalHeight())) setExtended(!isExtended());
            else if (isExtended()) {
                float offset = getOriginalHeight() + 4;
                for (String str : getModeStringSetting().getModes()) {
                    if (str.equalsIgnoreCase(getModeStringSetting().getValue())) continue;
                    if (MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY() + offset, 90, getOriginalHeight())) {
                        getModeStringSetting().setValue(str);
                        setExtended(false);
                    }
                    offset += getOriginalHeight();
                }
            }
        }
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public ModeStringSetting getModeStringSetting() {
        return modeStringSetting;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }
}
