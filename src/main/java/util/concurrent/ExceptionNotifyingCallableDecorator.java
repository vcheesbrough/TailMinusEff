package util.concurrent;

import java.util.concurrent.Callable;

public class ExceptionNotifyingCallableDecorator<V> implements Callable<V> {

    private final Callable<V> delegate;
    private final UnhandledExceptionConsumer callback;

    public ExceptionNotifyingCallableDecorator(Callable<V> delegate, UnhandledExceptionConsumer callback) {
        super();
        this.delegate = delegate;
        this.callback = callback;
    }

    @Override
    public V call() throws Exception {
        try {
            return delegate.call();
        } catch (final Throwable t) {
            this.callback.acceptException(Thread.currentThread(), t);
            throw t;
        }
    }

}
