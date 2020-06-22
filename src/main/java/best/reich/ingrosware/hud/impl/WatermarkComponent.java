package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.hud.annotation.ComponentManifest;
import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.client.gui.ScaledResolution;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.hud.Component;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@ComponentManifest(label = "Watermark", x = 2, y = 2, width = 58, height = 18)
public class WatermarkComponent extends Component {
    @Setting("Color")
    public Color color = new Color(255, 0, 255);

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (mc.world == null || mc.player == null)
            return;
        mc.fontRenderer.drawStringWithShadow(IngrosWare.INSTANCE.getLabel(), getX(), getY(), color.getRGB());
    }
}
