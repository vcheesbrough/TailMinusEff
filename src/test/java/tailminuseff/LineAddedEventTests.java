package tailminuseff;

import org.junit.*;

public class LineAddedEventTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void smokeTestToString() {
		new LineAddedEvent(this, "Hello").toString();
	}

	@After
	public void tearDown() throws Exception {
	}

}
