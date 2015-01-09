package tailminuseff;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.concurrent.*;

import mockit.*;

import org.junit.*;

public class MultiFileModelTests {

	private MultiFileModel target;
	@Mocked
	private FileMonitorFactory mockMonitorFactory;
	@Mocked
	private FileMonitor mockMonitor;
	@Mocked
	private ExecutorService mockFileExecutorService;

	@Before
	public void setUp() throws Exception {
		new MockUp<ApplicationExecutors>() {
			@Mock
			public ExecutorService getFilesExecutorService() {
				return mockFileExecutorService;
			}
		};

		new Expectations() {
			{
				FileMonitorFactory.createForFile((File) any);
				returns(mockMonitor);
			}
		};
		target = new MultiFileModel();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void closeInterruptsRunningFuture(@Mocked File file) throws InterruptedException, ExecutionException {
		new Expectations() {
			{
				mockFileExecutorService.submit((FileMonitor) any).cancel(true);
			}
		};
		final FileLineModel model = target.openFile(file);

		target.close(model);
	}

	@Test
	public void closeWaitsForFuturesResult(@Mocked File file) throws InterruptedException, ExecutionException {
		new Expectations() {
			{
				mockFileExecutorService.submit((FileMonitor) any).get();
			}
		};
		final FileLineModel model = target.openFile(file);

		target.close(model);
	}

	@Test
	public void openFileInvokesMonitorFactory(@Mocked File file) {
		target.openFile(file);
		new Verifications() {
			{
				FileMonitorFactory.createForFile(file);
			}
		};
	}

	@Test
	public void openFileReturnsNewFileLineModel(@Mocked File file) {
		assertNotNull(target.openFile(file));
	}

	@Test
	public void openFileSubmitsMonitorToExecutorService(@Mocked File file) {
		target.openFile(file);

		new Verifications() {
			{
				mockFileExecutorService.submit((FileMonitor) any);
				times = 1;
			}
		};
	}
}
