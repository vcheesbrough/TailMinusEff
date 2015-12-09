package tailminuseff;

import com.google.common.eventbus.EventBus;
import mockit.Mocked;
import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class ApplicationExecutorsTests {

    private ApplicationExecutors target;
    @Mocked
    private EventBus mockEventBus;

    @Before
    public void Setup() {
        target = new ApplicationExecutors(mockEventBus);
    }

    @Test
    public void getFilesExecutorServiceReturnsInstance() {
        assertNotNull(target.createFilesExecutorService());
    }

    @Test
    public void getGeneralExecutorServiceReturnsInstance() {
        assertNotNull(target.createGeneralExecutorService());
    }

    @Test
    public void getScheduledExecutorServiceReturnsInstance() {
        assertNotNull(target.createScheduledExecutorService());
    }
}
