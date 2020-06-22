package best.reich.ingrosware.event.impl.other;

import net.minecraft.entity.Entity;
import tcb.bces.event.Event;
import tcb.bces.event.EventType;

public class EntityChunkEvent extends Event {
    private final Entity entity;
    private EventType type;

    public EntityChunkEvent(EventType type, Entity entity) {
        this.entity = entity;
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Entity getEntity() {
        return entity;
    }

}
