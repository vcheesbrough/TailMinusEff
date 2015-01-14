package util.concurrent;

import java.util.concurrent.ExecutorService;

import mockit.Mocked;

public class ExceptionNotifyingExecutorServiceDecoratorTests extends AbstractExceptionNotifyingExecutorServiceTests<ExecutorService> {

	@Mocked
	private ExecutorService mockDelegate;

	@Override
	protected ExecutorService createTarget(ExecutorService mockDelegate, UnhandledExceptionConsumer mockConsumer) {
		return new ExceptionNotifyingExecutorServiceDecorator(mockDelegate, mockConsumer);
	}

	@Override
	protected ExecutorService getMockDelegate() {
		return mockDelegate;
	}

}
