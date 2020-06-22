package best.reich.ingrosware.hud.type;

import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.client.gui.ScaledResolution;
import best.reich.ingrosware.hud.Component;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class CustomComponent extends Component {
    private final String text;

    @Setting("Color")
    public Color color = new Color(255,0,255);

    public CustomComponent(String text) {
        this.text = text;
        setLabel(text);
        setWidth(mc.fontRenderer.getStringWidth(text));
        setHeight(mc.fontRenderer.FONT_HEIGHT + 5);
        setX(2);
        setY(2);
        setLabelHidden(true);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        mc.fontRenderer.drawStringWithShadow(text, getX(), getY(), color.getRGB());
    }
}
