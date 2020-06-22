package best.reich.ingrosware.event.impl.other;

import tcb.bces.event.EventCancellable;

public class WorldTimeEvent extends EventCancellable {

    private Long worldTime;

    public Long getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(long worldTime) {
        this.worldTime = worldTime;
    }
}
