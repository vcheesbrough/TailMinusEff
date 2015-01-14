package util.concurrent;

import java.util.concurrent.*;

import mockit.*;

import org.junit.Test;

public class ExceptionNotifyingScheduledExecutorServiceDecoratorTests extends AbstractExceptionNotifyingExecutorServiceTests<ScheduledExecutorService> {

	@Mocked
	private ScheduledExecutorService mockDelegate;

	@Override
	protected ScheduledExecutorService createTarget(ScheduledExecutorService mockDelegate, UnhandledExceptionConsumer mockConsumer) {
		return new ExceptionNotifyingScheduledExecutorServiceDecorator(mockDelegate, mockConsumer);
	}

	@Override
	protected ScheduledExecutorService getMockDelegate() {
		return mockDelegate;
	}

	@Test
	public void scheduleRunnableWithLongAndTimeUnitCallsDelegate(@Mocked Runnable mockAction) {
		new Expectations() {
			{
				mockDelegate.schedule(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)), 1, TimeUnit.SECONDS);
				times = 1;
			}
		};
		target.schedule(mockAction, 1, TimeUnit.SECONDS);
	}

	@Test
	public void scheduleCallableWithLongAndTimeUnitCallsDelegate(@Mocked Callable<String> mockAction) {
		new Expectations() {
			{
				mockDelegate.schedule(withInstanceLike(new ExceptionNotifyingCallableDecorator<String>(null, null)), 1, TimeUnit.SECONDS);
				times = 1;
			}
		};
		target.schedule(mockAction, 1, TimeUnit.SECONDS);
	}

	@Test
	public void scheduleAtFixedRateCallsDelegate(@Mocked Runnable mockAction) {
		new Expectations() {
			{
				mockDelegate.scheduleAtFixedRate(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)), 1, 1, TimeUnit.SECONDS);
				times = 1;
			}
		};
		target.scheduleAtFixedRate(mockAction, 1, 1, TimeUnit.SECONDS);
	}

	@Test
	public void scheduleWithFixedDelayCallsDelegate(@Mocked Runnable mockAction) {
		new Expectations() {
			{
				mockDelegate.scheduleWithFixedDelay(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)), 1, 1, TimeUnit.SECONDS);
				times = 1;
			}
		};
		target.scheduleWithFixedDelay(mockAction, 1, 1, TimeUnit.SECONDS);
	}

}
