package us.devs.ingrosware.hud.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
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
@ComponentManifest(label = "Ping", x = 10, y = 4, width = 100, height = 100, hidden = true)
public class PingComponent extends Component {
    @Setting("Color")
    public Color color = new Color(0x616161);

    public PingComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        final NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId());
        final String ping = networkPlayerInfo == null ? "0ms" : networkPlayerInfo.getResponseTime() + " ms";
        mc.fontRenderer.drawStringWithShadow("Ping: " + ChatFormatting.WHITE + ping, getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth("Ping: " + ChatFormatting.WHITE + ping));
    }
}
