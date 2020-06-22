package best.reich.ingrosware.event.impl.other;

import tcb.bces.event.Event;
import net.minecraft.world.chunk.Chunk;

public class ChunkLoadEvent extends Event {

    private final double x, z;
    private final boolean isFull;
    private final Chunk chunk;
    public ChunkLoadEvent(Chunk chunk,boolean isFull,double x, double z) {
        this.chunk = chunk;
        this.isFull = isFull;
        this.x = x;
        this.z = z;
    }

    public boolean isFull() {
        return isFull;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }
}
