package us.devs.ingrosware.module.types;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringEscapeUtils;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.module.IModule;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.setting.impl.ColorSetting;
import us.devs.ingrosware.setting.impl.StringSetting;
import us.devs.ingrosware.traits.Hideable;
import us.devs.ingrosware.traits.Stateable;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ToggleableModule implements IModule, Hideable, Stateable {
    private String label, suffix;
    private ModuleCategory category;
    private Color color;
    private int bind;
    boolean hidden, state;

    public final Minecraft mc = Minecraft.getMinecraft();

    public ToggleableModule() {
        if(getClass().isAnnotationPresent(Toggleable.class)) {
            Toggleable toggleable = getClass().getAnnotation(Toggleable.class);
            this.label = toggleable.label();
            this.category = toggleable.category();
            this.color = new Color(toggleable.color());
            this.bind = toggleable.bind();
            this.hidden = toggleable.hidden();
            this.state = toggleable.state();
        }
    }

    @Override
    public void init() {
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

    public int getBind() {
        return bind;
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
        this.bind = bind;
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
    public void save(JsonObject destination) {
        destination.addProperty("State", getState());
        destination.addProperty("Bind", getBind());
        destination.addProperty("Hidden", isHidden());
        if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this) != null) {
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this).forEach(property -> {
                if (property instanceof ColorSetting) {
                    final ColorSetting colorSetting = (ColorSetting) property;
                    destination.addProperty(property.getLabel(), colorSetting.getValue().getRGB());
                } else if (property instanceof StringSetting) {
                    final StringSetting stringSetting = (StringSetting) property;
                    final String escapedStr = StringEscapeUtils.escapeJava(stringSetting.getValue());
                    destination.addProperty(property.getLabel(), escapedStr);
                } else destination.addProperty(property.getLabel(), property.getValue().toString());
            });
        }
    }

    @Override
    public void load(JsonObject source) {
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
