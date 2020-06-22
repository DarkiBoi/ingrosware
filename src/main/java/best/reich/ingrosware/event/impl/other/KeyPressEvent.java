package best.reich.ingrosware.event.impl.other;

import tcb.bces.event.Event;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class KeyPressEvent extends Event {
    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
