package best.reich.ingrosware.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import tcb.bces.event.Event;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class Render2DEvent extends Event {
    private final float partialTicks;
    private final ScaledResolution scaledResolution;

    public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution= scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

}
