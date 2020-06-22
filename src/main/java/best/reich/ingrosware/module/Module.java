package best.reich.ingrosware.module;

import best.reich.ingrosware.traits.Configable;
import best.reich.ingrosware.traits.Labelable;
import tcb.bces.listener.IListener;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public interface Module extends Labelable, Configable, IListener {
    String getLabel();

    ModuleCategory getCategory();

    boolean isEnabled();
}
