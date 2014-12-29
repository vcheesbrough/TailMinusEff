package tailminuseff;

import static org.junit.Assert.*;

import org.junit.*;

public class LineAddedEventTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void smokeTestToString() {
		new LineAddedEvent(this, "Hello").toString();
	}

}
