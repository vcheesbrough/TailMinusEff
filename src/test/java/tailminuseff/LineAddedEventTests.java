package tailminuseff;

import tailminuseff.io.LineAddedEvent;
import org.junit.Test;

public class LineAddedEventTests {

    @Test
    public void smokeTestToString() {
        new LineAddedEvent(this, "Hello").toString();
    }
}
