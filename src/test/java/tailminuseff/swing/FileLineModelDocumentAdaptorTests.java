package tailminuseff.swing;

import tailminuseff.swing.FileLineModelDocumentAdaptor;
import java.util.*;
import javax.swing.text.*;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import tailminuseff.*;

public class FileLineModelDocumentAdaptorTests {

    @Mocked
    private Document mockDocument;

    @Mocked
    private FileLineModel mockFileLineModel;

    @Before
    public void setUp() throws Exception {
        new Expectations() {
            {
                mockFileLineModel.getLines();
                returns(Collections.EMPTY_LIST);
                mockFileLineModel.doInLock((Runnable) any);
                result = new Delegate() {
                    void delegate(Runnable action) {
                        action.run();
                    }
                };
            }
        };
    }

    @Test
    public void constructorAddsLinesIfModelNotEmpty() throws BadLocationException {
        final List<String> lines = Arrays.asList(new String[]{"One"});
        new Expectations() {
            {
                mockFileLineModel.getLines();
                returns(lines);
            }
        };

        new FileLineModelDocumentAdaptor(mockDocument, mockFileLineModel);

        new VerificationsInOrder() {
            {
                mockDocument.insertString(0, "One\n", null);
                times = 1;
            }
        };
    }

    @Test
    public void constructorRegistersListener() {
        new FileLineModelDocumentAdaptor(mockDocument, mockFileLineModel);
        new Verifications() {
            {
                mockFileLineModel.addListener((FileLineModelListener) any);
                times = 1;
            }
        };
    }

    @Test
    public void firstlineAddedEventAddsTextToDocument() throws BadLocationException {
        final List<FileLineModelListener> registeredListeners = new ArrayList<FileLineModelListener>();
        new Expectations() {
            {
                mockFileLineModel.addListener(withCapture(registeredListeners));
            }
        };
        new FileLineModelDocumentAdaptor(mockDocument, mockFileLineModel);

        registeredListeners.forEach(l -> l.lineAdded(new FileLineModelLineAddedEvent(mockFileLineModel, "Hello")));

        new Verifications() {
            {
                mockDocument.insertString(0, "Hello\n", null);
                times = 1;
            }
        };
    }

    @Test
    public void firstlineAddedEventWhenDocumentThrowsExceptionIsPropogated() throws BadLocationException {
        final List<FileLineModelListener> registeredListeners = new ArrayList<FileLineModelListener>();
        final BadLocationException thrownException = new BadLocationException("Hello\n", 0);
        new Expectations() {
            {
                mockFileLineModel.addListener(withCapture(registeredListeners));
                mockDocument.insertString(0, "Hello\n", null);
                result = thrownException;
            }
        };
        new FileLineModelDocumentAdaptor(mockDocument, mockFileLineModel);

        try {
            registeredListeners.forEach(l -> l.lineAdded(new FileLineModelLineAddedEvent(mockFileLineModel, "Hello")));
            fail("Exception Expected");
        } catch (final RuntimeException ex) {
            assertEquals(thrownException, ex.getCause());
        }
    }
}