package us.devs.ingrosware.hud.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.setting.annotation.Setting;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/20/2020
 **/
@ComponentManifest(label = "FPS", x = 2, y = 15, width = 100, height = 100, hidden = true)
public class FPSComponent extends Component {
    @Setting("Color")
    public Color color = new Color(0x616161);

    public FPSComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        mc.fontRenderer.drawStringWithShadow("FPS: " + ChatFormatting.WHITE + Minecraft.getDebugFPS(), getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth("FPS: " + Minecraft.getDebugFPS()));

    }
}
