package util.concurrent;

@FunctionalInterface
public interface UnhandledExceptionConsumer {

    void acceptException(Thread threa, Throwable throwable);
}
