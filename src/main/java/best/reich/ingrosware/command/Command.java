package best.reich.ingrosware.command;

import best.reich.ingrosware.command.annotation.CommandManifest;
import net.minecraft.client.Minecraft;
import best.reich.ingrosware.traits.Chatable;
import best.reich.ingrosware.traits.Labelable;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public abstract class Command implements Labelable, Chatable {
    private String label, description;
    private String[] handles;

    public final Minecraft mc = Minecraft.getMinecraft();

    public Command() {
        if(getClass().isAnnotationPresent(CommandManifest.class)) {
            CommandManifest commandManifest = getClass().getAnnotation(CommandManifest.class);
            this.label = commandManifest.label();
            this.description = commandManifest.description();
            this.handles = commandManifest.handles();
        }
    }

    public abstract void execute(String[] args);

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String[] getHandles() {
        return handles;
    }

}
