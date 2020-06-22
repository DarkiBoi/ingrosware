package best.reich.ingrosware.event.impl.render;

import net.minecraft.item.ItemStack;

import tcb.bces.event.EventCancellable;

public class RenderToolTipEvent extends EventCancellable {
    private ItemStack stack;
    private int x, y;

    public RenderToolTipEvent(ItemStack stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ItemStack getStack() {
        return stack;
    }
}
