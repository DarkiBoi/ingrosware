package best.reich.ingrosware.gui.hud.settings;

import best.reich.ingrosware.gui.hud.settings.components.HudSetting;
import best.reich.ingrosware.gui.hud.settings.components.impl.BooleanButton;
import best.reich.ingrosware.gui.hud.settings.components.impl.ColorButton;
import best.reich.ingrosware.gui.hud.settings.components.impl.NumberButton;
import best.reich.ingrosware.gui.hud.settings.components.impl.StringButton;
import best.reich.ingrosware.hud.Component;
import best.reich.ingrosware.setting.AbstractSetting;
import best.reich.ingrosware.setting.impl.BooleanSetting;
import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.setting.impl.NumberSetting;
import best.reich.ingrosware.setting.impl.StringSetting;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.gui.hud.GuiHudEditor;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class HudSettings extends GuiScreen {
    private best.reich.ingrosware.hud.Component hudComp;
    private ArrayList<HudSetting> components = new ArrayList<>();

    public HudSettings(Component hudComp) {
        this.hudComp = hudComp;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        components.clear();
        int y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2 - 80;
        if(IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(hudComp) != null) {
            for(AbstractSetting setting : IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(hudComp)) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanValue = (BooleanSetting) setting;
                    components.add(new BooleanButton(booleanValue, this.width / 2 - 90, y));
                    y += 16;
                }
                if (setting instanceof NumberSetting) {
                    NumberSetting numberValue = (NumberSetting) setting;
                    components.add(new NumberButton(numberValue, this.width / 2 - 90, y));
                    y += 16;
                }
                if(setting instanceof StringSetting) {
                    StringSetting stringValue = (StringSetting) setting;
                    components.add(new StringButton(stringValue,width / 2 - 90,y));
                    y += 26;
                }
                if(setting instanceof ColorSetting) {
                    ColorSetting colorSetting = (ColorSetting) setting;
                    components.add(new ColorButton(colorSetting,width / 2 - 90,y));
                    y += 26;
                }
            }
        }


        components.forEach(HudSetting::init);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        RenderUtil.drawBordered(scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2 - 100, scaledResolution.getScaledWidth() / 2 + 100, scaledResolution.getScaledHeight() / 2 + 100, 0.5, new Color(15, 15, 15, 255).getRGB(), new Color(15, 15, 15, 255).getRGB());        RenderUtil.drawRect(scaledResolution.getScaledWidth() / 2 + 86,scaledResolution.getScaledHeight() / 2 - 96,10,10,new Color(0xFF4548).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("x",scaledResolution.getScaledWidth() / 2 + 88.5f,scaledResolution.getScaledHeight() / 2 - 95, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(hudComp.getLabel(), scaledResolution.getScaledWidth() / 2 - RenderUtil.getStringWidth(hudComp.getLabel()) / 2, scaledResolution.getScaledHeight() / 2 - 95, -1);
        components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        if (mouseWithinBounds(mouseX,mouseY,scaledResolution.getScaledWidth() / 2 + 86,scaledResolution.getScaledHeight() / 2 - 96,10,10)) {
            mc.displayGuiScreen(new GuiHudEditor());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        components.forEach(component -> component.keyTyped(typedChar,keyCode));
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

}
