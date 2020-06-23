package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.setting.annotation.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.hud.Component;
import best.reich.ingrosware.hud.annotation.ComponentManifest;

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

    @Setting("Format")
    public String format = "FPS: %s";

    public FPSComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        mc.fontRenderer.drawStringWithShadow(String.format(format, "" + ChatFormatting.WHITE + Minecraft.getDebugFPS()), getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth(String.format(format, "" + ChatFormatting.WHITE + Minecraft.getDebugFPS())));

    }
}
