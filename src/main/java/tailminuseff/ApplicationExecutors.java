package tailminuseff;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.*;
import javax.inject.*;
import util.concurrent.*;

@Singleton
public class ApplicationExecutors {

    private final EventBus eventBus;

    @Inject
    public ApplicationExecutors(EventBus eventBus) {
        this.unhandledExceptionConsumer = (thread, throwable) -> {
            eventBus.post(new UnhandledException(throwable, thread, "Unknown error"));
        };
        this.eventBus = eventBus;
    }

    public ExecutorService createFilesExecutorService() {
        final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("FileExecutorService-Pool-%d");
        threadFactoryBuilder.setDaemon(true);
        threadFactoryBuilder.setPriority(Thread.MIN_PRIORITY);
        return new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);

    }

    public ExecutorService createGeneralExecutorService() {
        final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("GeneralExecutorService-Pool-%d");
        return new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);
    }

    public ScheduledExecutorService createScheduledExecutorService() {
        final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("ScheduledExecutorService-Pool-%d");
        return new ExceptionNotifyingScheduledExecutorServiceDecorator(Executors.newScheduledThreadPool(1, threadFactoryBuilder.build()), unhandledExceptionConsumer);
    }

    private final UnhandledExceptionConsumer unhandledExceptionConsumer;
}
