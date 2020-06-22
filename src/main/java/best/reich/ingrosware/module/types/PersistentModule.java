package best.reich.ingrosware.module.types;

import best.reich.ingrosware.module.annotation.Persistent;
import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.setting.impl.StringSetting;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringEscapeUtils;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class PersistentModule implements Module {
    private String label;
    private ModuleCategory category;

    public final Minecraft mc = Minecraft.getMinecraft();

    public PersistentModule() {
        if(getClass().isAnnotationPresent(Persistent.class)) {
            Persistent persistent = getClass().getAnnotation(Persistent.class);
            this.label = persistent.label();
            this.category = persistent.category();
        }

        IngrosWare.INSTANCE.getBus().register(this);
        IngrosWare.INSTANCE.getSettingManager().scan(this);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public ModuleCategory getCategory() {
        return category;
    }

    @Override
    public JsonObject toJson() {
        final JsonObject object = new JsonObject();
        if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this) != null) {
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this).forEach(property -> {
                if (property instanceof ColorSetting) {
                    final ColorSetting colorSetting = (ColorSetting) property;
                    object.addProperty(property.getLabel(), colorSetting.getValue().getRGB());
                } else if (property instanceof StringSetting) {
                    final StringSetting stringSetting = (StringSetting) property;
                    final String escapedStr = StringEscapeUtils.escapeJava(stringSetting.getValue());
                    object.addProperty(property.getLabel(), escapedStr);
                } else object.addProperty(property.getLabel(), property.getValue().toString());
            });
        }

        return object;
    }

    @Override
    public void fromJson(JsonObject source) {
        if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this) != null) {
            source.entrySet().forEach(entry -> IngrosWare.INSTANCE.getSettingManager().getSetting(this, entry.getKey()).ifPresent(property -> {
                if (property instanceof ColorSetting) {
                    final ColorSetting colorSetting = (ColorSetting) property;
                    colorSetting.setValue(entry.getValue().getAsString());
                } else if (property instanceof StringSetting) {
                    final StringSetting stringSetting = (StringSetting) property;
                    stringSetting.setValue(StringEscapeUtils.unescapeJava(entry.getValue().getAsString()));
                } else property.setValue(entry.getValue().getAsString());
            }));
        }
    }

    @Override
    public boolean isEnabled() {
        return mc.player != null;
    }
}
