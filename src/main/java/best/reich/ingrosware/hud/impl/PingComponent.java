package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.setting.annotation.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
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
@ComponentManifest(label = "Ping", x = 10, y = 4, width = 100, height = 100, hidden = true)
public class PingComponent extends Component {
    @Setting("Color")
    public Color color = new Color(0x616161);

    @Setting("Format")
    public String format = "Ping: %s";

    public PingComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        final NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId());
        final String ping = networkPlayerInfo == null ? "0ms" : networkPlayerInfo.getResponseTime() + " ms";
        mc.fontRenderer.drawStringWithShadow(String.format(format, "" + ChatFormatting.WHITE + ping), getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth(String.format(format, "" + ChatFormatting.WHITE + ping)));
    }
}
