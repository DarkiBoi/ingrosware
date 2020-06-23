package best.reich.ingrosware.gui.hud.settings.components.impl;

import best.reich.ingrosware.setting.impl.StringSetting;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.hud.settings.components.HudSetting;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class StringButton extends HudSetting {
    private final StringSetting stringSetting;
    private boolean editing;
    private String content = "";

    public StringButton(StringSetting stringSetting, float posX, float posY) {
        super(stringSetting.getLabel(), posX, posY);
        this.stringSetting = stringSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean isHovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(),
                Minecraft.getMinecraft().fontRenderer.getStringWidth(isEditinig() ? content : getLabel() + ": " + StringUtils.capitalize(stringSetting.getValue().toLowerCase())) + 4, 10);

        RenderUtil.drawBorderedRect(getPosX(), getPosY(),
                Minecraft.getMinecraft().fontRenderer.getStringWidth(isEditinig() ? content : getLabel() + ": " +
                        StringUtils.capitalize(stringSetting.getValue().toLowerCase())) + 4, 10, 0.5F, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(isEditinig() ? content : getLabel() + ": " + stringSetting.getValue(),
                getPosX() + 2, getPosY() + getHeight() - 1 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), Minecraft.getMinecraft().fontRenderer.getStringWidth(getLabel() + ": " +
                StringUtils.capitalize(stringSetting.getValue().toLowerCase())) + 4, 10)) {
            if(mouseButton == 0) {
                setEditinig(!isEditinig());
                if (isEditinig()) {
                    content = stringSetting.getValue();
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (isEditinig()) {
            String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
            if (key == Keyboard.KEY_BACK) {
                if (content.length() > 1) {
                    content = content.substring(0, content.length() - 1);
                } else if (content.length() == 1) {
                    content = "";
                }
            } else if (key == Keyboard.KEY_RETURN) {
                stringSetting.setValue(content);
                content = "";
                setEditinig(false);
            } else if (Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar) || specialChars.contains(Character.toString(typedChar))) {
                if (RenderUtil.getStringWidth(content) < 230) {
                    content += Character.toString(typedChar);
                }
            }
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void setEditinig(boolean editinig) {
        this.editing = editinig;
    }

    private boolean isEditinig() {
        return editing;
    }
}
