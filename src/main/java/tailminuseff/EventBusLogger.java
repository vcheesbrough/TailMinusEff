package tailminuseff;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventBusLogger {

    private static final Logger logger = Logger.getLogger("tailminuseff.eventbus");

    @Inject
    public EventBusLogger(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void receiveAnything(Object event) {
        logger.fine(event.toString());
    }

}
