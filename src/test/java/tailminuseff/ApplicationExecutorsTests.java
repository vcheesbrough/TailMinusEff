package tailminuseff;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.*;

public class ApplicationExecutorsTests {

	@Test
	public void getFilesExecutorServiceReturnsInstance() {
		assertNotNull(ApplicationExecutors.getFilesExecutorService());
	}

	@Test
	public void getFilesExecutorServiceReturnsSameInstanceEachTime() {
		assertSame(ApplicationExecutors.getFilesExecutorService(), ApplicationExecutors.getFilesExecutorService());
	}

	@Test
	public void getGeneralExecutorServiceReturnsInstance() {
		assertNotNull(ApplicationExecutors.getGeneralExecutorService());
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
