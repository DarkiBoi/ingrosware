package best.reich.ingrosware.module;

import best.reich.ingrosware.traits.Labelable;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public enum ModuleCategory implements Labelable {
    COMBAT("Combat"),
    OTHER("Other"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    RENDER("Render");

    String label;

    ModuleCategory(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
