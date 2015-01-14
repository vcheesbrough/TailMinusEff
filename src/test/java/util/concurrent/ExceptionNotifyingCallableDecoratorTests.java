package util.concurrent;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.Callable;

import mockit.*;

import org.junit.Test;

public class ExceptionNotifyingCallableDecoratorTests {

	@Test
	public void callCallsCall(@Mocked Callable<String> mockDelegate, @Mocked UnhandledExceptionConsumer mockConsumer) throws Exception {
		new Expectations() {
			{
				mockDelegate.call();
				times = 1;
			}
		};
		new ExceptionNotifyingCallableDecorator<String>(mockDelegate, mockConsumer).call();
	}

	@Test
	public void whenCheckedExceptionConsumerIsInvoked(@Mocked Callable<String> mockDelegate, @Mocked UnhandledExceptionConsumer mockConsumer) throws Exception {
		new Expectations() {
			{
				mockDelegate.call();
				result = new IOException("simulated");
				times = 1;
				mockConsumer.acceptException((Thread) any, (IOException) any);
			}
		};
		try {
			new ExceptionNotifyingCallableDecorator<String>(mockDelegate, mockConsumer).call();
			fail("Exception expected");
		} catch (final IOException ioex) {
		}
	}

}
