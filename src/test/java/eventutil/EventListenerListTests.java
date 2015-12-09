package eventutil;

import java.util.*;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventListenerListTests {

    private EventListenerList<TestListener> target;

    @Before
    public void setUp() throws Exception {
        target = new TestableEventListenerList<EventListenerListTests.TestListener>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void forEachListenerInvokesOnEachRegisteredListener(@Mocked TestListener mockListenerOne, @Mocked TestListener mockListenerTwo) {
        getTargetListeners().add(mockListenerOne);
        getTargetListeners().add(mockListenerTwo);

        final EventObjectOne evt = new EventObjectOne(target);
        target.forEachLisener(listener -> listener.eventOne(evt));

        new Verifications() {
            {
                mockListenerOne.eventOne(evt);
                times = 1;
                mockListenerTwo.eventOne(evt);
                times = 1;
            }
        };
    }

    @Test
    public void forEachListenerInvokesOnEachRegisteredListenerInOrder(@Mocked TestListener mockListenerOne, @Mocked TestListener mockListenerTwo) {
        getTargetListeners().add(mockListenerOne);
        getTargetListeners().add(mockListenerTwo);

        final EventObjectOne evt = new EventObjectOne(target);
        target.forEachLisener(listener -> listener.eventOne(evt));

        new VerificationsInOrder() {
            {
                mockListenerOne.eventOne(evt);
                times = 1;
                mockListenerTwo.eventOne(evt);
                times = 1;
            }
        };
    }

    private List<TestListener> getTargetListeners() {
        return ((TestableEventListenerList<TestListener>) target).getListeners();
    }

    @Test
    public void testAddListener(@Mocked TestListener testListener) {
        target.addListener(testListener);
        assertTrue(getTargetListeners().contains(testListener));
    }

    @Test
    public void testRemoveListener(@Mocked TestListener testListener) {
        getTargetListeners().add(testListener);
        target.removeListener(testListener);
        assertFalse(getTargetListeners().contains(testListener));
    }

    private static class EventObjectOne extends EventObject {

        private static final long serialVersionUID = 706653594384232162L;

        public EventObjectOne(Object source) {
            super(source);
        }
    }

    private static class TestableEventListenerList<ListenerType extends EventListener> extends EventListenerList<ListenerType> {

        public List<ListenerType> getListeners() {
            return this.listeners;
        }
    }

    public static interface TestListener extends EventListener {

        void eventOne(EventObjectOne evt);
    }
}
