package tailminuseff.config;

import java.awt.Rectangle;
import java.beans.*;
import java.util.LinkedList;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class ConfigurationTests {

    private @Mocked
    PropertyChangeListener allPropertiesMockListener;
    private Configuration target;

    @Before
    public void Setup() {
        target = new Configuration();
        target.addPropertyChangeListener(allPropertiesMockListener);
    }

    @After
    public void TearDown() {
        target.removePropertyChangeListener(allPropertiesMockListener);
    }

    @Test
    public void mainWindowBoundsFiresEventsIfChanged(@Mocked PropertyChangeListener mainWindowBoundsPropertyChangeListener) {
        target.setMainWindowBounds(new Rectangle(1, 2, 3, 4));
        target.addPropertyChangeListener(Configuration.MAIN_WINDOW_BOUNDS, mainWindowBoundsPropertyChangeListener);

        final LinkedList<PropertyChangeEvent> capturedEvents = new LinkedList<PropertyChangeEvent>();
        new Expectations() {
            {
                mainWindowBoundsPropertyChangeListener.propertyChange(withCapture(capturedEvents));
            }
        };
        target.setMainWindowBounds(new Rectangle(5, 6, 7, 8));

        assertEquals(1, capturedEvents.size());
        assertEquals(new Rectangle(1, 2, 3, 4), capturedEvents.peekFirst().getOldValue());
        assertEquals(new Rectangle(5, 6, 7, 8), capturedEvents.peekFirst().getNewValue());
        target.removePropertyChangeListener(Configuration.MAIN_WINDOW_BOUNDS, mainWindowBoundsPropertyChangeListener);
    }
}
