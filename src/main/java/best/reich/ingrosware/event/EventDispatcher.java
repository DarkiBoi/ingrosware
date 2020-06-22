package best.reich.ingrosware.event;

import tcb.bces.bus.compilation.Dispatcher;
import tcb.bces.event.Event;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class EventDispatcher extends Dispatcher {

    @Override
    public <T extends Event> T dispatchEvent(T event) {
        Dispatcher.dispatch();
        return event;
    }
}
