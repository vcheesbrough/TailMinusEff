package tailminuseff.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.*;

import javax.swing.text.*;

import mockit.*;

import org.junit.*;

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
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constructorThrowsExceptionIfModelNotEmpty() {
		final List<String> lines = Arrays.asList(new String[] { "One", "Two" });
		new Expectations() {
			{
				mockFileLineModel.getLines();
				returns(lines);
			}
		};
		try {
			new FileLineModelDocumentAdaptor(mockDocument, mockFileLineModel);
			fail("Exception was expected");
		} catch (final Exception ex) {
			// expected
		}
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

		registeredListeners.forEach(l -> l.lineAdded(new FileLineModelLineAddedEvent(mockFileLineModel, "Hello\n")));

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
			registeredListeners.forEach(l -> l.lineAdded(new FileLineModelLineAddedEvent(mockFileLineModel, "Hello\n")));
			fail("Exception Expected");
		} catch (final RuntimeException ex) {
			assertEquals(thrownException, ex.getCause());
		}
	}
}
