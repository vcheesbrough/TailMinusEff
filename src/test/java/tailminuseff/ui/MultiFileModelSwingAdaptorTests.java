package tailminuseff.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.SwingUtilities;

import mockit.*;

import org.junit.*;

import tailminuseff.*;

import com.google.common.util.concurrent.MoreExecutors;

public class MultiFileModelSwingAdaptorTests {

	@Mocked
	MultiFileModel mockDelegate;

	private MultiFileModelSwingAdaptor target;

	@Mocked
	private MultiFileModelSwingAdaptorListener mockListener;

	@Before
	public void setUp() throws Exception {
		new MockUp<SwingUtilities>() {
			@Mock
			public void invokeLater(Runnable doRun) {
				doRun.run();
			}
		};

		new MockUp<ApplicationExecutors>() {
			@Mock
			public ExecutorService getGeneralExecutorService() {
				return MoreExecutors.sameThreadExecutor();
			}
		};

		target = new MultiFileModelSwingAdaptor();
		target.addListener(mockListener);
	}

	@After
	public void tearDown() throws Exception {
		target.removeListener(mockListener);
	}

	@Test
	public void openCallsOpen(@Mocked File file) {
		target.openFile(file);

		new Verifications() {
			{
				mockDelegate.openFile(file);
				times = 1;
			}
		};
	}

	@Test
	public void closeWithFileDelegatesToDelegate(@Mocked File file) throws InterruptedException, ExecutionException {
		target.closeFile(file);
		new Verifications() {
			{
				mockDelegate.close(file);
				times = 1;
			}
		};
	}

	@Test
	public void closeGeneratesEvent(@Mocked File file) throws InterruptedException, ExecutionException {
		target.closeFile(file);
		new Verifications() {
			{
				mockListener.fileClosed((FileClosedEvent) any);
				times = 1;
			}
		};
	}

	@Test
	public void closeGeneratesEventWithModel(@Mocked File file) throws InterruptedException, ExecutionException {
		target.closeFile(file);
		new Verifications() {
			{
				FileClosedEvent evt;
				mockListener.fileClosed(evt = withCapture());
				assertNotNull(evt.getFileLineModel());
			}
		};
	}

	@Test
	public void openGeneratesEventWithDelegateReturnValue(@Mocked File file) {
		final List<FileOpenedEvent> capturedEvents = new ArrayList<FileOpenedEvent>();
		new Expectations() {
			{
				mockListener.fileOpened(withCapture(capturedEvents));
				times = 1;
			}
		};

		target.openFile(file);

		assertEquals(file, capturedEvents.get(0).getFileLineModel().getFile());
	}

	@Test
	public void getInstqnceReturnsSameInstanceEachTime() {
		assertEquals(MultiFileModelSwingAdaptor.getInstance(), MultiFileModelSwingAdaptor.getInstance());
	}

}
