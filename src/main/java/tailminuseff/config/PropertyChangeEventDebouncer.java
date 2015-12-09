package tailminuseff.config;

import eventutil.EventListenerList;
import java.beans.*;
import java.util.concurrent.*;
import javax.inject.Inject;

public class PropertyChangeEventDebouncer {

    private final long maximumEventFrequencyMs = 500;

    private final Object lock = new Object();

    private final EventListenerList<PropertyChangeListener> outputEventListeners = new EventListenerList<PropertyChangeListener>(lock);

    private final ScheduledExecutorService scheduledExecutorService;

    @Inject
    public PropertyChangeEventDebouncer(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public PropertyChangeListener getInputListener() {
        return inputListener;
    }

    private ScheduledFuture<Void> future;

    private final PropertyChangeListener inputListener = evt -> {
        if (future != null) {
            future.cancel(false);
        }
        future = scheduledExecutorService.schedule(() -> {
            synchronized (lock) {
                final PropertyChangeEvent newEvt = new PropertyChangeEvent(PropertyChangeEventDebouncer.this, null, null, null);
                outputEventListeners.forEachLisener(listener -> listener.propertyChange(newEvt));
                return null;
            }
        }, maximumEventFrequencyMs, TimeUnit.MILLISECONDS);
    };

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        outputEventListeners.addListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        outputEventListeners.removeListener(listener);
    }
}
