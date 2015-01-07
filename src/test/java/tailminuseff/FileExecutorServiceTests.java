package tailminuseff;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.*;

public class FileExecutorServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getExecutorServiceReturnsInstance() {
		assertNotNull(FileExecutorService.getExecutorService());
	}

	@Test
	public void getExecutorServiceReturnsSameInstanceEachTime() {
		assertSame(FileExecutorService.getExecutorService(), FileExecutorService.getExecutorService());
	}

}
