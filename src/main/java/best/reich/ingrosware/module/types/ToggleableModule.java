package best.reich.ingrosware.module.types;

import best.reich.ingrosware.keybind.Keybind;
import best.reich.ingrosware.keybind.task.impl.ToggleModuleTask;
import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.setting.impl.StringSetting;
import best.reich.ingrosware.traits.Hideable;
import best.reich.ingrosware.traits.Stateable;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringEscapeUtils;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.module.ModuleCategory;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ToggleableModule implements Module, Hideable, Stateable {
    private String label, suffix;
    private ModuleCategory category;
    private Color color;
    private Keybind keybind;
    boolean hidden, state;

    public final Minecraft mc = Minecraft.getMinecraft();

    public ToggleableModule() {
        if(getClass().isAnnotationPresent(Toggleable.class)) {
            Toggleable toggleable = getClass().getAnnotation(Toggleable.class);
            this.label = toggleable.label();
            this.category = toggleable.category();
            this.color = new Color(toggleable.color());
            this.keybind = new Keybind(toggleable.bind(), new ToggleModuleTask(this));
            this.hidden = toggleable.hidden();
            this.state = toggleable.state();
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

    public Keybind getKeybind() {
        return keybind;
    }

    public int getBind() {
        return getKeybind().getKey();
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;

        if(state) {
            onState();
        } else {
            onDisable();
        }
    }

    public void setBind(int bind) {
        this.keybind.setKey(bind);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void onState() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public JsonObject toJson() {
        final JsonObject object = new JsonObject();
        object.addProperty("State", getState());
        object.addProperty("Bind", getBind());
        object.addProperty("Hidden", isHidden());
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
        if (source.has("State") && source.get("State").getAsBoolean()) {
            setState(true);
        }
        if (source.has("Bind")) {
            setBind(source.get("Bind").getAsInt());
        }
        if (source.has("Hidden") && source.get("Hidden").getAsBoolean()) {
            setHidden(true);
        }
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
        return getState() && mc.player != null;
    }
}
