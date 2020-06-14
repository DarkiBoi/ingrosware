package us.devs.ingrosware.hud.impl;

import net.minecraft.client.gui.ScaledResolution;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.setting.annotation.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@ComponentManifest(label = "Arraylist",x = 2, y = 22, width = 70, height = 18)
public class ArraylistComponent extends Component {

    @Setting("Rainbow")
    public boolean rainbow = false;

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        if (mc.world == null || mc.player == null)
            return;
        final ArrayList<ToggleableModule> sorted = new ArrayList<>(IngrosWare.INSTANCE.getModuleManager().getToggles());
        float y = getY();
        sorted.sort(Comparator.comparingDouble(m -> -mc.fontRenderer.getStringWidth(m.getLabel())));
        for (ToggleableModule module : sorted) {
            if (module.getState() && !module.isHidden()) {
                mc.fontRenderer.drawStringWithShadow(module.getLabel(), getX() + ((getX() + getWidth() / 2) > (scaledResolution.getScaledWidth() >> 1) ? (getWidth() - mc.fontRenderer.getStringWidth(module.getLabel())) : 0), y + ((getY() + getHeight() / 2) > (scaledResolution.getScaledHeight() >> 1) ? getHeight() - mc.fontRenderer.FONT_HEIGHT : 0), rainbow ? getRainbow(6000, (int) (y * 30), 0.85f):module.getColor().getRGB());
                y += ((getY() + getHeight() / 2) > scaledResolution.getScaledHeight() >> 1) ? -mc.fontRenderer.FONT_HEIGHT : mc.fontRenderer.FONT_HEIGHT;
            }
        }
    }
    public static int getRainbow(int speed, int offset,float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();
    }
}
