package util.concurrent;

import java.util.concurrent.ExecutorService;

public class ExceptionNotifyingExecutorServiceDecorator extends AbstractExceptionNotifyingExecutorService<ExecutorService> {

    public ExceptionNotifyingExecutorServiceDecorator(ExecutorService delegate, UnhandledExceptionConsumer uncaughtConsumer) {
        super(delegate, uncaughtConsumer);
        // TODO Auto-generated constructor stub
    }
}
