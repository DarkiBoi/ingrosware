package us.devs.ingrosware.module;

import com.google.gson.JsonObject;
import tcb.bces.listener.IListener;
import us.devs.ingrosware.traits.Configable;
import us.devs.ingrosware.traits.Labelable;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public interface IModule extends Labelable, Configable, IListener {
    String getLabel();

    ModuleCategory getCategory();

    void init();

    boolean isEnabled();
}
