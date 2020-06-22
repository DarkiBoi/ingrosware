package best.reich.ingrosware.event.impl.other;

import tcb.bces.event.Event;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class FullScreenEvent extends Event {
    private float width, height;

    public FullScreenEvent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
