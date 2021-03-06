package tailminuseff;

import tailminuseff.io.FileResetEvent;
import tailminuseff.io.FileMonitorListener;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.LineAddedEvent;
import tailminuseff.io.FileMonitorFactory;
import java.io.File;
import java.util.LinkedList;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class FileLineModelTests {

    @Mocked
    private FileMonitor mockMonitor;
    private FileMonitorListener inputListener;
    private FileLineModel target;
    @Mocked
    private FileLineModelListener mockedListener;
    @Mocked
    private FileMonitorFactory mockMonitorFactory;

    @Before
    public void Setup() {
        final LinkedList<FileMonitorListener> captures = new LinkedList<FileMonitorListener>();
        new Expectations() {
            {
                mockMonitor.addListener(withCapture(captures));
            }
        };
        target = new FileLineModel(mockMonitor);
        inputListener = captures.peekFirst();
        target.addListener(mockedListener);
    }

    @After
    public void Teardown() {
        target.removeListener(mockedListener);
    }

    @Test
    public void getFileReturnsFileFromMonitor(@Mocked File file) {
        new Expectations() {
            {
                mockMonitor.getFile();
                returns(file);
            }
        };
        assertEquals(file, target.getFile());
    }

    @Test
    public void doInLockCallsRunOnPassedParameter(@Mocked Runnable action) {
        target.doInLock(action);
        new Verifications() {
            {
                action.run();
                times = 1;
            }
        };
    }

    @Test
    public void ConstructorAddsListener() {
        new Verifications() {
            {
                mockMonitor.addListener((FileMonitorListener) any);
                times = 1;
            }
        };
    }

    @Test
    public void lineEventAddsLine() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        assertEquals(1, target.getLines().size());
        assertEquals("Hello World", target.getLines().get(0));
    }

    @Test
    public void lineEventFiresEvent() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        new Verifications() {
            {
                mockedListener.lineAdded((FileLineModelLineAddedEvent) any);
                times = 1;
            }
        };
    }

    @Test
    public void lineEventFiresEventWithCorrectLine() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        new Verifications() {
            {
                FileLineModelLineAddedEvent evt;
                mockedListener.lineAdded(evt = withCapture());
                times = 1;
                assertEquals("Hello World", evt.getLine());
            }
        };
    }

    @Test
    public void lineEventFiresEventWithCorrectSource() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        new Verifications() {
            {
                FileLineModelLineAddedEvent evt;
                mockedListener.lineAdded(evt = withCapture());
                times = 1;
                assertEquals(target, evt.getSource());
            }
        };
    }

    @Test
    public void resetEventClearsLines() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        inputListener.fileReset(new FileResetEvent(mockMonitor));
        assertEquals(0, target.getLines().size());
    }

    @Test
    public void resetEventFiresEvent() {
        inputListener.lineRead(new LineAddedEvent(mockMonitor, "Hello World"));
        inputListener.fileReset(new FileResetEvent(mockMonitor));
        new Verifications() {
            {
                mockedListener.reset((FileLineModelResetEvent) any);
                times = 1;
            }
        };
    }
}
