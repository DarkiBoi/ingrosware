package us.devs.ingrosware.module.types;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.module.IModule;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
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
    private String label;
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

    @Override
    public void onState() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void save(JsonObject destination) {

    }

    @Override
    public void load(JsonObject source) {

    }

    @Override
    public boolean isEnabled() {
        return getState() && mc.player != null;
    }
}
