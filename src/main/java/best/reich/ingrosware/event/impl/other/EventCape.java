package best.reich.ingrosware.event.impl.other;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import tcb.bces.event.EventCancellable;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/17/2020
 **/
public class EventCape extends EventCancellable {
    private AbstractClientPlayer player;
    private ResourceLocation resourceLocation;

    public EventCape(AbstractClientPlayer player) {
        this.player = player;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}
