package tailminuseff;

import com.google.common.eventbus.*;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StackTraceDumpingEventBusConsumer {

    private final EventBus eventBus;

    @Inject
    public StackTraceDumpingEventBusConsumer(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void dumpExceptionToConsole(UnhandledException ex) {
        System.out.println(ex.getMessage() + " in thread " + ex.getThread());
        ex.getThrown().printStackTrace();
    }
}
