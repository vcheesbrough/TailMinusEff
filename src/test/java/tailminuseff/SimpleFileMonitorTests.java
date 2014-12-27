package tailminuseff;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleFileMonitorTests {

	private File file;
	private SimpleFileMonitor target;
	@Mocked
	private FileMonitorListener mockListener;
	private TestListener testListener;

	@Before
	public void Setup() throws IOException {
		file = File.createTempFile("SimpleFileMonitorTests", "");
		file.deleteOnExit();
		target = new SimpleFileMonitor();
		target.addListener(mockListener);
		testListener = new TestListener();
		target.addListener(testListener);
	}

	@Test
	public void emptyFileDoesNotCallListener() throws Exception {
		new Expectations() {
			{
				mockListener.lineRead((FileMonitorEvent) any);
				times = 0;
			}
		};
		target.GetFileMonitorCallable(file).call();
	}

	@Test
	public void singleLineFileCallsListenerOnce() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");
		new Expectations() {
			{
				mockListener.lineRead((FileMonitorEvent) any);
				times = 1;
			}
		};
		target.GetFileMonitorCallable(file).call();
	}

	@Test
	public void singleLineFileCallsListenerWithCorrectEventSource()
			throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");
		target.GetFileMonitorCallable(file).call();

		assertEquals(target, testListener.GetNextEvent().getSource());
	}

	@Test
	public void singleLineFileCallsListenerWithCorrectLine() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");

		target.GetFileMonitorCallable(file).call();

		assertEquals("Hello World", testListener.GetNextEvent().getLine());
	}

	@Test
	public void twoLineFileCallsListenerWithCorrectLine() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\nAnother Line\n");
		target.GetFileMonitorCallable(file).call();

		assertEquals("Hello World", testListener.GetNextEvent().getLine());
		assertEquals("Another Line", testListener.GetNextEvent().getLine());
	}

	@Test
	@Ignore
	public void lineAddedToFileCallsListenerOnce() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");

		ExecutorService service = Executors.newCachedThreadPool();
		Future<Void> result = service.submit(target
				.GetFileMonitorCallable(file));

		assertEquals("Hello World", testListener.GetNextEvent().getLine());

		FileUtils.writeStringToFile(file, "Another Line\n");

		assertEquals("Another Line", testListener.GetNextEvent().getLine());
	}

	private class TestListener implements FileMonitorListener {
		private final BlockingQueue<FileMonitorEvent> events = new LinkedBlockingQueue<FileMonitorEvent>();

		@Override
		public void lineRead(FileMonitorEvent evt) {
			if (!events.offer(evt)) {
				throw new IllegalStateException();
			}
		}

		public FileMonitorEvent GetNextEvent() throws InterruptedException {
			FileMonitorEvent evt = events.poll(500, TimeUnit.MILLISECONDS);
			assertNotNull(evt);
			return evt;
		}
	}
}
