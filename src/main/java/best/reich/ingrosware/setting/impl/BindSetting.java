package best.reich.ingrosware.setting.impl;

import best.reich.ingrosware.setting.AbstractSetting;

import java.lang.reflect.Field;

/**
 * made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/16/2020
 **/
public class BindSetting extends AbstractSetting<Integer> {
    private boolean pressed;

    public BindSetting(String label, Object object, Field field, boolean pressed) {
        super(label, object, field);
        this.pressed = pressed;
    }

    @Override
    public void setValue(String value) {
        try {
            final int i = Integer.parseInt(value);
            setValue(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
