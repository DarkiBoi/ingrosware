package best.reich.ingrosware.setting;

import best.reich.ingrosware.setting.annotation.*;
import best.reich.ingrosware.setting.annotation.Setting;
import best.reich.ingrosware.setting.impl.*;
import com.esotericsoftware.reflectasm.FieldAccess;
import best.reich.ingrosware.manager.impl.AbstractMapManager;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class SettingManager extends AbstractMapManager<Object, List<AbstractSetting>> {

    public void register(Object object, AbstractSetting setting) {
        getRegistry().computeIfAbsent(object, collection -> new ArrayList<>());
        getRegistry().get(object).add(setting);
    }

    public void scan(Object object) {
        FieldAccess access = FieldAccess.get(object.getClass());
        for (Field field : object.getClass().getDeclaredFields()) {
            boolean accessibility = field.isAccessible();
            if (field.isAnnotationPresent(best.reich.ingrosware.setting.annotation.Setting.class)) {
                field.setAccessible(true);
                best.reich.ingrosware.setting.annotation.Setting setting = field.getAnnotation(Setting.class);
                try {
                    Object val = access.get(object, field.getName());
                    if (val instanceof Boolean) {
                        register(object, new BooleanSetting(setting.value(), object, field));
                    }

                    if (val instanceof String) {
                        if (field.isAnnotationPresent(Mode.class)) {
                            Mode mode = field.getAnnotation(Mode.class);
                            register(object, new ModeStringSetting(setting.value(), object, field, mode.value()));
                        } else {
                            register(object, new StringSetting(setting.value(), object, field));
                        }
                    }

                    if (val instanceof Color) {
                        register(object, new ColorSetting(setting.value(), object, field));
                    }


                    if (field.isAnnotationPresent(Bind.class)) {
                        Bind bind = field.getAnnotation(Bind.class);
                        if (val instanceof Integer) {
                            register(object, new BindSetting(setting.value(), object, field, bind.pressed()));
                        }
                    }

                    if (field.isAnnotationPresent(Clamp.class)) {
                        Clamp clamp = field.getAnnotation(Clamp.class);

                        /* We have to do this to determine the number property's type. */
                        if (val instanceof Integer) {
                            register(object, new NumberSetting(setting.value(), object, field, Integer.parseInt(clamp.minimum()), Integer.parseInt(clamp.maximum()), Integer.parseInt(clamp.inc())));
                        } else if (val instanceof Double) {
                            register(object, new NumberSetting<>(setting.value(), object, field, Double.parseDouble(clamp.minimum()), Double.parseDouble(clamp.maximum()), Integer.parseInt(clamp.inc())));
                        } else if (val instanceof Float) {
                            register(object, new NumberSetting<>(setting.value(), object, field, Float.parseFloat(clamp.minimum()), Float.parseFloat(clamp.maximum()), Integer.parseInt(clamp.inc())));
                        } else if (val instanceof Long) {
                            register(object, new NumberSetting<>(setting.value(), object, field, Long.parseLong(clamp.minimum()), Long.parseLong(clamp.maximum()), Integer.parseInt(clamp.inc())));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                field.setAccessible(accessibility);
            }
        }
    }

    public Optional<AbstractSetting> getSetting(Object object, String label) {
        Optional<AbstractSetting> found = Optional.empty();
        for (AbstractSetting property : getSettingsFromObject(object)) {
            if (property.getLabel().equalsIgnoreCase(label)) {
                found = Optional.of(property);
                break;
            }
        }

        return found;
    }

    public List<AbstractSetting> getSettingsFromObject(Object object) {
        if (getRegistry().get(object) != null) {
            return getRegistry().get(object);
        } else {
            return null;
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void start() {
    }
}
