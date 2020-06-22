package best.reich.ingrosware.event.impl.other;

import net.minecraft.client.gui.ScaledResolution;
import tcb.bces.event.Event;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ResizeEvent extends Event {
    private ScaledResolution sr;

    public ResizeEvent(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getSr() {
        return sr;
    }
}
