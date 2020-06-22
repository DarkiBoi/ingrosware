package best.reich.ingrosware.profile;

import best.reich.ingrosware.traits.Labelable;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/17/2020
 **/
public enum Ranks implements Labelable {
    DEVELOPER("Developer", new Color(0xD440CF)),
    BETA("Beta", new Color(0xC67756));

    private final String label;
    private final Color color;

    Ranks(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}
