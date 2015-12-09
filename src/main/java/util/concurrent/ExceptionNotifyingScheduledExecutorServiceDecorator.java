package util.concurrent;

import java.util.concurrent.*;

public class ExceptionNotifyingScheduledExecutorServiceDecorator extends AbstractExceptionNotifyingExecutorService<ScheduledExecutorService> implements ScheduledExecutorService {

    public ExceptionNotifyingScheduledExecutorServiceDecorator(ScheduledExecutorService delegate, UnhandledExceptionConsumer uncaughtConsumer) {
        super(delegate, uncaughtConsumer);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return delegate.schedule(createDecorator(command), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return delegate.schedule(createDecorator(callable), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return delegate.scheduleAtFixedRate(createDecorator(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return delegate.scheduleWithFixedDelay(createDecorator(command), initialDelay, delay, unit);
    }
}
