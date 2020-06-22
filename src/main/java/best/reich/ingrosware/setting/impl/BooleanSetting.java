package best.reich.ingrosware.setting.impl;

import best.reich.ingrosware.setting.AbstractSetting;

import java.lang.reflect.Field;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class BooleanSetting extends AbstractSetting<Boolean> {

    public BooleanSetting(String label, Object object, Field field) {
        super(label, object, field);
    }

    @Override
    public void setValue(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on")) {
            setValue(true);
        } else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off")) {
            setValue(false);
        }
    }

    public void toggle() {
        setValue(!getValue());
    }
}
