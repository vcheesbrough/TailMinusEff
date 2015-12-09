package eventutil;

import java.util.EventListener;

public interface EventProducer<ListenerType extends EventListener> {

    void addListener(ListenerType listener);

    void removeListener(ListenerType listener);
}
