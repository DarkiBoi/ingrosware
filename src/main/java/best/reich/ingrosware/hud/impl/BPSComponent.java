package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.setting.annotation.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.hud.Component;
import best.reich.ingrosware.hud.annotation.ComponentManifest;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/20/2020
 **/
@ComponentManifest(label = "BPS", x = 2, y = 4, width = 100, height = 100, hidden = true)
public class BPSComponent extends Component {
    @Setting("Color")
    public Color color = new Color(0x616161);

    @Setting("Format")
    public String format = "BPS: %s";

    public BPSComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        final DecimalFormat df = new DecimalFormat("#.#");
        final double deltaX = mc.player.posX - mc.player.prevPosX;
        final double deltaZ = mc.player.posZ - mc.player.prevPosZ;
        final float tickRate = (mc.timer.tickLength / 1000.0f);
        final String bps = df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate));

        mc.fontRenderer.drawStringWithShadow(String.format(format, "" + ChatFormatting.WHITE + bps), getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth(String.format(format, "" + ChatFormatting.WHITE + bps)));
    }
}
