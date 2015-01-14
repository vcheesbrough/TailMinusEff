package tailminuseff.config;

import java.beans.*;
import java.util.concurrent.*;

import tailminuseff.ApplicationExecutors;
import eventutil.EventListenerList;

public class PropertyChangeEventDebouncer {

	private final long maximumEventFrequencyMs = 150;

	private final Object lock = new Object();

	private final EventListenerList<PropertyChangeListener> outputEventListeners = new EventListenerList<PropertyChangeListener>(lock);

	public PropertyChangeListener getInputListener() {
		return inputListener;
	}

	private ScheduledFuture<Void> future;

	private final PropertyChangeListener inputListener = evt -> {
		if (future != null) {
			future.cancel(false);
		}
		future = ApplicationExecutors.getScheduledExecutorService().schedule(() -> {
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