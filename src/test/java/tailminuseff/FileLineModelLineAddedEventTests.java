package tailminuseff;

import mockit.Mocked;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FileLineModelLineAddedEventTests {

    @Test
    public void getLineReturnsLine(@Mocked FileLineModel model) {
        assertEquals("Hello", new FileLineModelLineAddedEvent(model, "Hello").getLine());
    }

    @Test
    public void smokeTestToString(@Mocked FileLineModel model) {
        new FileLineModelLineAddedEvent(model, "Hello").toString();
    }
}
