package util.concurrent;

import java.util.Collections;
import java.util.concurrent.*;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractExceptionNotifyingExecutorServiceTests<DelegateType extends ExecutorService> {

    private DelegateType mockDelegate;

    @Mocked
    protected UnhandledExceptionConsumer mockConsumer;

    protected DelegateType target;

    protected abstract DelegateType createTarget(DelegateType mockDelegate, UnhandledExceptionConsumer mockConsumer);

    protected abstract DelegateType getMockDelegate();

    @Before
    public void setUp() throws Exception {
        this.mockDelegate = getMockDelegate();
        this.target = createTarget(mockDelegate, mockConsumer);
    }

    @Test
    public void executeRunnableCallsDelegate(@Mocked Runnable runnable) {
        target.execute(runnable);
        new Verifications() {
            {
                mockDelegate.execute(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)));
                times = 1;
            }
        };
    }

    @Test
    public void shutdownCallsDelegate() {
        target.shutdown();
        new Verifications() {
            {
                mockDelegate.shutdown();
                times = 1;
            }
        };
    }

    @Test
    public void shutdownNowCallsDelegate() {
        target.shutdownNow();
        new Verifications() {
            {
                mockDelegate.shutdownNow();
                times = 1;
            }
        };
    }

    @Test
    public void isShutdownCallsDelegate() {
        new Expectations() {
            {
                mockDelegate.isShutdown();
                result = true;
                times = 1;
            }
        };
        assertTrue(target.isShutdown());
    }

    @Test
    public void isTerminatedCallsDelegate() {
        new Expectations() {
            {
                mockDelegate.isTerminated();
                result = true;
                times = 1;
            }
        };
        assertTrue(target.isTerminated());
    }

    @Test
    public void awaitTerminationCallsDelegate() throws InterruptedException {
        new Expectations() {
            {
                mockDelegate.awaitTermination(1, (TimeUnit) any);
                result = true;
            }
        };
        assertTrue(target.awaitTermination(1, TimeUnit.SECONDS));
        new Verifications() {
            {
                mockDelegate.awaitTermination(1, TimeUnit.SECONDS);
                times = 1;
            }
        };
    }

    @Test
    public void submitCallableCallsDelegate(@Mocked Callable<String> callable) {
        target.submit(callable);
        new Verifications() {
            {
                mockDelegate.submit(withInstanceLike(new ExceptionNotifyingCallableDecorator<String>(null, null)));
                times = 1;
            }
        };
    }

    @Test
    public void submitRunnableAndResultCallsDelegate(@Mocked Runnable runnable) {
        target.submit(runnable, "Hello World");
        new Verifications() {
            {
                mockDelegate.submit(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)), "Hello World");
                times = 1;
            }
        };
    }

    @Test
    public void submitRunnableCallsDelegate(@Mocked Runnable runnable) {
        target.submit(runnable);
        new Verifications() {
            {
                mockDelegate.submit(withInstanceLike(new ExceptionNotifyingRunnableDecorator(null, null)));
                times = 1;
            }
        };
    }

    @Test
    public void invokeAllCollectionOfCallableIsNotImplementedYet(@Mocked Callable<String> callable) throws InterruptedException {
        try {
            target.invokeAll(Collections.singleton(callable));
            fail("Expected exception");
        } catch (final UnsupportedOperationException ex) {
        }
    }

    @Test
    public void invokeAllCollectionCallableWithTLongAndTimeUnitIsNotImplementedYet(@Mocked Callable<String> callable) throws InterruptedException {
        try {
            target.invokeAll(Collections.singleton(callable), 1, TimeUnit.SECONDS);
            fail("Expected exception");
        } catch (final UnsupportedOperationException ex) {
        }
    }

    @Test
    public void invokeAnyCollectionCallableIsNotImplementedYet(@Mocked Callable<String> callable) throws InterruptedException, ExecutionException {
        try {
            target.invokeAny(Collections.singleton(callable));
            fail("Expected exception");
        } catch (final UnsupportedOperationException ex) {
        }
    }

    @Test
    public void invokeAnyCollectionCallableWithLongAndTimeUnitIsNotImplementedYet(@Mocked Callable<String> callable) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            target.invokeAny(Collections.singleton(callable), 1, TimeUnit.SECONDS);
            fail("Expected exception");
        } catch (final UnsupportedOperationException ex) {
        }
    }
}
