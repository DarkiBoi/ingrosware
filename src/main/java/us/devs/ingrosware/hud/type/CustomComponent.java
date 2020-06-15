package us.devs.ingrosware.hud.type;

import net.minecraft.client.gui.ScaledResolution;
import us.devs.ingrosware.hud.Component;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class CustomComponent extends Component {
    private final String text;
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
        mc.fontRenderer.drawStringWithShadow(text, getX(), getY(), -1);
    }
}
