package best.reich.ingrosware.event.impl.other;

import net.minecraft.block.Block;
import tcb.bces.event.EventCancellable;

public final class BlockCollideEvent extends EventCancellable {

    private final Block block;

    public BlockCollideEvent(Block block) {
        this.block = block;
    }

    public final Block getBlock() {
        return this.block;
    }
}
