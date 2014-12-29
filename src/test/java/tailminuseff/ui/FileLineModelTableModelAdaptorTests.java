package tailminuseff.ui;

import static org.junit.Assert.assertEquals;

import java.util.*;

import javax.swing.SwingUtilities;
import javax.swing.event.*;

import mockit.*;

import org.junit.*;

import tailminuseff.*;

public class FileLineModelTableModelAdaptorTests {

	private FileLineModelTableModelAdaptor target;
	@Mocked
	private FileLineModel mockLineModel;
	private FileLineModelListener inputListener;
	@Mocked
	private TableModelListener mockTableModelListener;

	@Before
	public void setUp() throws Exception {
		new MockUp<SwingUtilities>() {
			@Mock
			public void invokeLater(Runnable doRun) {
				doRun.run();
			}
		};
		final LinkedList<FileLineModelListener> captures = new LinkedList<FileLineModelListener>();
		new Expectations() {
			{
				mockLineModel.addListener(withCapture(captures));
			}
		};
		target = new FileLineModelTableModelAdaptor(mockLineModel);
		inputListener = captures.peekFirst();
		target.addTableModelListener(mockTableModelListener);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getRowCountReturnsNumberOfLinesFromModel() {
		new Expectations() {
			{
				final List<String> lines = new ArrayList<String>();
				lines.add("First Line");
				lines.add("Second Line");
				mockLineModel.getLines();
				returns(lines);
			}
		};
		assertEquals(2, target.getRowCount());
	}

	@Test
	public void lineEventFirstTableDataChanged() {
		inputListener.lineAdded(new FileLineModelLineAddedEvent(mockLineModel, "Hello World"));

		new Verifications() {
			{
				mockTableModelListener.tableChanged((TableModelEvent) any);
			}
		};
	}

	@Test
	public void resetEventFirstTableDataChanged() {
		inputListener.reset(new FileLineModelResetEvent(mockLineModel));

		new Verifications() {
			{
				mockTableModelListener.tableChanged((TableModelEvent) any);
			}
		};
	}

	@Test
	public void testGetColumnCount() {
		assertEquals(1, target.getColumnCount());
	}

	@Test
	public void getValueAtReturnsValueFromModelIfInRange() {
		new Expectations() {
			{
				final List<String> lines = new ArrayList<String>();
				lines.add("First Line");
				lines.add("Second Line");
				mockLineModel.getLines();
				returns(lines);
			}
		};
		assertEquals("First Line", target.getValueAt(0, 0));
		assertEquals("Second Line", target.getValueAt(1, 0));
	}
}