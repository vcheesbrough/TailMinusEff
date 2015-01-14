package util.concurrent;

import static org.junit.Assert.fail;
import mockit.*;

import org.junit.Test;

public class ExceptionNotifyingRunnableDecoratorTests {

	@Test
	public void runCallsRun(@Mocked Runnable mockDelegate, @Mocked UnhandledExceptionConsumer mockConsumer) {
		new Expectations() {
			{
				mockDelegate.run();
				times = 1;
			}
		};
		new ExceptionNotifyingRunnableDecorator(mockDelegate, mockConsumer).run();
	}

	@Test
	public void whenRuntimeExceptionConsumerIsInvoked(@Mocked Runnable mockDelegate, @Mocked UnhandledExceptionConsumer mockConsumer) {
		new Expectations() {
			{
				mockDelegate.run();
				result = new NullPointerException("SIMULATED");
				times = 1;
				mockConsumer.acceptException((Thread) any, (NullPointerException) any);
				times = 1;
			}
		};
		try {
			new ExceptionNotifyingRunnableDecorator(mockDelegate, mockConsumer).run();
			fail("Exception expected");
		} catch (final NullPointerException npe) {
		}
	}

	@Test
	public void whenErrorConsumerIsInvoked(@Mocked Runnable mockDelegate, @Mocked UnhandledExceptionConsumer mockConsumer) {
		new Expectations() {
			{
				mockDelegate.run();
				result = new UnknownError("SIMULATED");
				times = 1;
				mockConsumer.acceptException((Thread) any, (UnknownError) any);
				times = 1;
			}
		};
		try {
			new ExceptionNotifyingRunnableDecorator(mockDelegate, mockConsumer).run();
			fail("Exception expected");
		} catch (final UnknownError oops) {
		}
	}
}
