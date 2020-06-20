package us.devs.ingrosware.hud.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.setting.annotation.Setting;
import us.devs.ingrosware.util.render.RenderUtil;

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
        calculateSize();
        final ArrayList<ToggleableModule> sorted = new ArrayList<>(IngrosWare.INSTANCE.getModuleManager().getToggles());
        float y = getY();
        sorted.sort(Comparator.comparingDouble(m -> -RenderUtil.getStringWidth(m.getLabel() + (m.getSuffix() != null ? ChatFormatting.GRAY + " [" + StringUtils.capitalize(m.getSuffix().toLowerCase()) + "]":""))));
        for (ToggleableModule module : sorted) {
            if (module.getState() && !module.isHidden()) {
                mc.fontRenderer.drawStringWithShadow(module.getLabel() + (module.getSuffix() != null ? ChatFormatting.GRAY + " [" + StringUtils.capitalize(module.getSuffix().toLowerCase()) + "]":""), getX() + ((getX() + getWidth() / 2) > (scaledResolution.getScaledWidth() >> 1) ? (getWidth() - RenderUtil.getStringWidth(module.getLabel() + (module.getSuffix() != null ? ChatFormatting.GRAY + " [" + StringUtils.capitalize(module.getSuffix().toLowerCase()) + "]":""))) : 0), y + ((getY() + getHeight() / 2) > (scaledResolution.getScaledHeight() >> 1) ? getHeight() - RenderUtil.getStringHeight() : 0), rainbow ? getRainbow(6000, (int) (y * 30), 0.85f):module.getColor().getRGB());
                y += ((getY() + getHeight() / 2) > scaledResolution.getScaledHeight() >> 1) ? -RenderUtil.getStringHeight() : RenderUtil.getStringHeight();
            }
        }
    }

    private int getRainbow(int speed, int offset,float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();
    }

    private void calculateSize() {
        int width = 0, height = 0;

        for(ToggleableModule toggleableModule : IngrosWare.INSTANCE.getModuleManager().getToggles()) {
            if(toggleableModule.getState() && !toggleableModule.isHidden()) {
                int itemWidth = mc.fontRenderer.getStringWidth(toggleableModule.getLabel()) + 7;
                if (itemWidth > width)
                    width = itemWidth;
                height += 9F;
            }
        }

        if (width == 0)
            width = 100;
        if (height == 0)
            height = ((int) 9F);


        this.setWidth(width);
        this.setHeight(height);
    }
}
