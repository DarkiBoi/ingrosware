package best.reich.ingrosware.setting.impl;

import best.reich.ingrosware.setting.AbstractSetting;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ColorSetting extends AbstractSetting<Color> {

    public ColorSetting(String label, Object object, Field field) {
        super(label, object, field);
    }

    public void setValue(int value) {
        setValue(new Color(value));
    }

    @Override
    public void setValue(String value) {
        setValue(new Color(Integer.parseInt(value)));
    }
}

