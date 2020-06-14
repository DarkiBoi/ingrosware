package us.devs.ingrosware.hud;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.traits.Configable;
import us.devs.ingrosware.traits.Hideable;
import us.devs.ingrosware.traits.Labelable;

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
    public void save(JsonObject directory) {
        directory.addProperty("x", x);
        directory.addProperty("y", y);
        directory.addProperty("hidden", hidden);
        if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this) != null)
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(this).forEach(property -> directory.addProperty(property.getLabel(), property.getValue().toString()));
    }

    @Override
    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
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
            directory.entrySet().forEach(entry -> IngrosWare.INSTANCE.getSettingManager().getSetting(this, entry.getKey()).ifPresent(property -> property.setValue(entry.getValue().getAsString())));
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
