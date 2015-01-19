package tailminuseff;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.*;

public class ApplicationExecutorsTests {
	private ApplicationExecutors target;

	@Before
	public void Setup() {
		target = new ApplicationExecutors();
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
