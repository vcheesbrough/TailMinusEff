package tailminuseff;

import static org.junit.Assert.*;
import mockit.Mocked;

import org.junit.*;

public class FileLineModelLineAddedEventTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void smokeTestToString(@Mocked FileLineModel model) {
		new FileLineModelLineAddedEvent(model, "Hello").toString();
	}

	@Test
	public void getLineReturnsLine(@Mocked FileLineModel model) {
		assertEquals("Hello", new FileLineModelLineAddedEvent(model, "Hello").getLine());
	}

}
