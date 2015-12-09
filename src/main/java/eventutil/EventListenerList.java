package eventutil;

import java.util.*;
import java.util.function.Consumer;

public class EventListenerList<ListenerType extends EventListener> {

    protected final List<ListenerType> listeners = Collections.synchronizedList(new ArrayList<ListenerType>());

    private final Object lock;

    public EventListenerList() {
        this(new Object());
    }

    public EventListenerList(Object lock) {
        this.lock = lock;
    }

    public void addListener(ListenerType listener) {
        synchronized (lock) {
            listeners.add(listener);
        }
    }

    public void forEachLisener(Consumer<? super ListenerType> action) {
        synchronized (lock) {
            new ArrayList<ListenerType>(this.listeners).forEach(action);
        }
    }

    public void removeListener(ListenerType listener) {
        synchronized (lock) {
            listeners.remove(listener);
        }
    }
}
