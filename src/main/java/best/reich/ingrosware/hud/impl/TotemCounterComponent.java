package best.reich.ingrosware.hud.impl;

import best.reich.ingrosware.setting.annotation.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
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
@ComponentManifest(label = "TotemCounter", x = 2, y = 10, width = 100, height = 100, hidden = true)
public class TotemCounterComponent extends Component {
    @Setting("Color")
    public Color color = new Color(0x616161);

    @Setting("Format")
    public String format = "Totems: %s";

    public TotemCounterComponent() {
        setHeight(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        mc.fontRenderer.drawStringWithShadow(String.format(format, "" + ChatFormatting.WHITE + totemCount()), getX(), getY(), color.getRGB());
        setWidth(mc.fontRenderer.getStringWidth(String.format(format, "" + ChatFormatting.WHITE + totemCount())));
    }

    private int totemCount() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.player.inventory.getStackInSlot(i).isEmpty() && mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }
}
