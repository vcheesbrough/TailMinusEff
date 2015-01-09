package tailminuseff;

import org.junit.Test;

public class LineAddedEventTests {

	@Test
	public void smokeTestToString() {
		new LineAddedEvent(this, "Hello").toString();
	}
}
