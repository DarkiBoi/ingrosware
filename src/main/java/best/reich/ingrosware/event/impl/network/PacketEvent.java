package best.reich.ingrosware.event.impl.network;

import net.minecraft.network.Packet;
import tcb.bces.event.Event;
import tcb.bces.event.EventCancellable;
import tcb.bces.event.EventType;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class PacketEvent extends EventCancellable {
    private Packet packet;
    private EventType type;

    public PacketEvent(EventType type, Packet packet) {
        this.packet = packet;
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Packet getPacket() {
        return packet;
    }
}
