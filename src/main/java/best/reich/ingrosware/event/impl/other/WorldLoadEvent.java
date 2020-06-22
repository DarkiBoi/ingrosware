package best.reich.ingrosware.event.impl.other;

import tcb.bces.event.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class WorldLoadEvent extends Event {
    private WorldClient worldClient;

    public WorldLoadEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

    public WorldClient getWorldClient() {
        return worldClient;
    }
}
