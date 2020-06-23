package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.setting.annotation.Setting;
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
@ComponentManifest(label = "NetherCoords", x = 2, y = 0, width = 100, height = 100)
public class NetherCoords extends Component {

    @Setting("Color")
    public Color color = new Color(0xAE3A43);

    @Setting("Format (example: xyz: %s, %s, %s)")
    public String format = "x: %s, y: %s, z: %s";


    public NetherCoords() {
        setY(new ScaledResolution(mc).getScaledHeight() - 14);
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        final float x = Math.round(mc.player.posX * 8);
        final float y = Math.round(mc.player.posY);
        final float z = Math.round(mc.player.posZ * 8);

        mc.fontRenderer.drawStringWithShadow(String.format(format, x, y, z), getX(),
                getY() - ((getY() + getHeight() > scaledResolution.getScaledHeight() -
                        10 && mc.ingameGUI.getChatGUI().getChatOpen()) ? 12 : 0), color.getRGB());
        if (getWidth() != mc.fontRenderer.getStringWidth(String.format(format, x, y, z))) {
            setWidth(mc.fontRenderer.getStringWidth(String.format(format, x, y, z)));
        }
    }
}
