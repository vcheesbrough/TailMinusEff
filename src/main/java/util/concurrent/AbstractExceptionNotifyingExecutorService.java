package util.concurrent;

import java.util.*;
import java.util.concurrent.*;

public class AbstractExceptionNotifyingExecutorService<DelegateType extends ExecutorService> implements ExecutorService {

    protected final DelegateType delegate;
    protected final UnhandledExceptionConsumer uncaughtConsumer;

    public AbstractExceptionNotifyingExecutorService(DelegateType delegate, UnhandledExceptionConsumer uncaughtConsumer) {
        super();
        this.delegate = delegate;
        this.uncaughtConsumer = uncaughtConsumer;
    }

    protected Runnable createDecorator(Runnable runnable) {
        return new ExceptionNotifyingRunnableDecorator(runnable, uncaughtConsumer);
    }

    protected <V> Callable<V> createDecorator(Callable<V> callable) {
        return new ExceptionNotifyingCallableDecorator<V>(callable, uncaughtConsumer);
    }

    @Override
    public void execute(Runnable command) {
        delegate.execute(createDecorator(command));
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(createDecorator(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(createDecorator(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(createDecorator(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
