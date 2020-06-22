package best.reich.ingrosware.hud;

import best.reich.ingrosware.hud.annotation.ComponentManifest;
import best.reich.ingrosware.setting.impl.ColorSetting;
import best.reich.ingrosware.setting.impl.StringSetting;
import best.reich.ingrosware.traits.Configable;
import best.reich.ingrosware.traits.Hideable;
import best.reich.ingrosware.traits.Labelable;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringEscapeUtils;
import best.reich.ingrosware.IngrosWare;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class Component implements Labelable, Hideable, Configable {
    private float x, y, width, height, lastX, lastY;
    public String label;
    private boolean hidden, dragging, labelHidden;

    public Minecraft mc = Minecraft.getMinecraft();
    private ScaledResolution sr = new ScaledResolution(mc);

    public Component() {
        if (getClass().isAnnotationPresent(ComponentManifest.class)) {
            ComponentManifest componentManifest = getClass().getAnnotation(ComponentManifest.class);
            this.label = componentManifest.label();
            this.x = componentManifest.x();
            this.y = componentManifest.y();
            this.width = componentManifest.width();
            this.height = componentManifest.height();
            this.hidden = componentManifest.hidden();
        }
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void init() {
        IngrosWare.INSTANCE.getSettingManager().scan(this);
    }

    public void onDraw(ScaledResolution scaledResolution) {

    }

    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledWidth() < scaledResolution.getScaledWidth() && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(scaledResolution.getScaledWidth() - getWidth() - 2);
        }
        if (sr.getScaledHeight() < scaledResolution.getScaledHeight() && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(scaledResolution.getScaledHeight() - getHeight() - 2);
        }
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight() || sr.getScaledWidth() != scaledResolution.getScaledWidth()) {
            sr = scaledResolution;
        }
    }

    public void onFullScreen(float w, float h) {
        if (sr.getScaledWidth() < w && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(w - (sr.getScaledWidth() - getWidth()) - 2);
        }
        if (sr.getScaledHeight() < h && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(h - (sr.getScaledHeight() - getHeight()) - 2);
        }
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() || sr.getScaledWidth() != new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }

    @Override
    public JsonObject toJson() {
        final JsonObject object = new JsonObject();
        object.addProperty("x", x);
        object.addProperty("y", y);
        object.addProperty("hidden", hidden);
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
        source.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name":
                    return;
                case "x":
                    setX(data.getValue().getAsInt());
                    return;
                case "y":
                    setY(data.getValue().getAsInt());
                    return;
                case "hidden":
                    setHidden(data.getValue().getAsBoolean());
                    return;
                default:break;
            }
        });
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


    public void setLabelHidden(boolean labelHidden) {
        this.labelHidden = labelHidden;
    }

    public boolean isLabelHidden() {
        return labelHidden;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
