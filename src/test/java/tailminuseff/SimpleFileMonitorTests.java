package tailminuseff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import junit.framework.AssertionFailedError;
import mockit.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class SimpleFileMonitorTests {

	private File file;
	private SimpleFileMonitor target;
	@Mocked
	private FileMonitorListener mockListener;
	private TestListener testListener;
	private ExecutorService executorService;
	private CompletionService<Void> completionService;

	@Before
	public void Setup() throws IOException {
		file = File.createTempFile("SimpleFileMonitorTests", "");
		file.deleteOnExit();
		target = new SimpleFileMonitor(file);
		target.addListener(mockListener);
		testListener = new TestListener();
		target.addListener(testListener);
		executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("SimpleFileMonitorTests-Pool-%d").build());
		completionService = new ExecutorCompletionService<Void>(executorService);
	}

	@After
	public void Teardown() throws Exception {
		if (this.executorService != null) {
			final List<Runnable> notExecutedTasks = this.executorService.shutdownNow();
			if (notExecutedTasks.size() > 0) {
				fail("Some submitted tasks were not executed");
			}
		}

		if (this.completionService != null) {
			for (Future<Void> future = this.completionService.poll(); future != null; future = this.completionService.poll()) {
				future.get();
			}
		}

		if (file != null) {
			Files.deleteIfExists(file.toPath());
		}
	}

	@Test
	public void singleLineFileCallsListenerOnce() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");
		new Expectations() {
			{
				mockListener.lineRead((LineAddedEvent) any);
				times = 1;
			}
		};
		this.completionService.submit(target);
		this.testListener.getNextEventAsLine();
	}

	@Test
	public void singleLineFileCallsListenerWithCorrectEventSource() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");

		this.completionService.submit(target);

		assertEquals(target, testListener.getNextEventAsLine().getSource());
	}

	@Test
	public void singleLineFileCallsListenerWithCorrectLine() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\n");

		this.completionService.submit(target);

		assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
	}

	@Test
	public void twoLineFileCallsListenerWithCorrectLine() throws Exception {
		FileUtils.writeStringToFile(file, "Hello World\nAnother Line\n");

		this.completionService.submit(target);

		assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
		assertEquals("Another Line\n", testListener.getNextEventAsLine().getLine());
	}

	@Test
	public void lineAddedToFileCallsListenerOnce() throws Exception {
		Files.write(file.toPath(), "Hello World\n".getBytes(), StandardOpenOption.APPEND);

		this.completionService.submit(target);

		assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());

		Files.write(file.toPath(), "Another Line\n".getBytes(), StandardOpenOption.APPEND);

		assertEquals("Another Line\n", testListener.getNextEventAsLine().getLine());
	}

	@Test
	public void windowsLineEndingsSplitLinesAlso() throws Exception {
		Files.write(file.toPath(), "Hello World\r\n".getBytes(), StandardOpenOption.APPEND);

		this.completionService.submit(target);

		assertEquals("Hello World\r\n", testListener.getNextEventAsLine().getLine());

	}

	@Test
	public void lineAddedInTwoPartsIsReadAsOneLine() throws Exception {
		Files.write(file.toPath(), "Hello ".getBytes(), StandardOpenOption.APPEND);

		this.completionService.submit(target);

		try {
			final LineAddedEvent evt = testListener.getNextEventAsLine();
			fail("Did not expect event " + evt);
		} catch (final TimeoutException tex) {
		}

		Files.write(file.toPath(), "World\n".getBytes(), StandardOpenOption.APPEND);
		assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
	}

	@Test
	public void lotsOfLinesAddedToFileCallsListenerMultipleTimes() throws Exception {
		Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);

		this.completionService.submit(target);

		assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());

		final int lineCount = 100;

		this.completionService.submit(() -> {
			try (OutputStream writer1 = new FileOutputStream(file, true)) {
				for (int i = 0; i < lineCount; i++) {
					writer1.write(("Another Line " + i + "\n").getBytes());
				}
			}
			return null;
		});
		for (int i = 0; i < lineCount; i++) {
			assertEquals("Another Line " + i + "\n", testListener.getNextEventAsLine().getLine());
		}
	}

	@Test
	public void removedListenerDoesNotGetInvoked() throws Exception {
		this.completionService.submit(target);

		Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);
		assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());
		target.removeListener(testListener);

		Files.write(file.toPath(), "SecondLine\n".getBytes(), StandardOpenOption.APPEND);
		try {
			final LineAddedEvent evt = testListener.getNextEventAsLine();
			fail("Did not expect event " + evt);
		} catch (final TimeoutException tex) {
		}
	}

	@Test
	public void deletedFileGeneratesResetEvent() throws Exception {
		this.completionService.submit(target);

		Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);
		assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());

		Files.delete(file.toPath());
		testListener.getNextEventAsReset();
	}

	@Test
	public void lineWrittenAfterDeleteGeneratesLineEvent() throws Exception {
		this.completionService.submit(target);
		Files.write(file.toPath(), "".getBytes(), StandardOpenOption.APPEND);

		Files.delete(file.toPath());
		testListener.getNextEventAsReset();

		Files.write(file.toPath(), "ThirdLine\n".getBytes(), StandardOpenOption.CREATE_NEW);
		assertEquals("ThirdLine\n", testListener.getNextEventAsLine().getLine());
	}

	private class TestListener implements FileMonitorListener {
		private final BlockingQueue<Object> event = new LinkedBlockingQueue<>();
		private final List<Object> allEvents = new LinkedList<Object>();

		@Override
		public void lineRead(LineAddedEvent evt) {
			allEvents.add(evt);
			if (!event.offer(evt)) {
				throw new IllegalStateException();
			}
		}

		@Override
		public void fileReset(FileResetEvent evt) {
			allEvents.add(evt);
			if (!event.offer(evt)) {
				throw new IllegalStateException();
			}
		}

		public LineAddedEvent getNextEventAsLine() throws InterruptedException, TimeoutException {
			Object obj = waitForNextEvent();
			if (obj instanceof LineAddedEvent) {
				final LineAddedEvent evt = (LineAddedEvent) obj;
				return evt;
			} else {
				throw new AssertionFailedError("Next event was not a line");
			}
		}

		public FileResetEvent getNextEventAsReset() throws InterruptedException, TimeoutException {
			Object obj = waitForNextEvent();
			if (obj instanceof FileResetEvent) {
				final FileResetEvent evt = (FileResetEvent) obj;
				return evt;
			} else {
				throw new AssertionFailedError("Next event was not a reset");
			}
		}

		private Object waitForNextEvent() throws InterruptedException, TimeoutException {
			Object obj = event.poll(200, TimeUnit.MILLISECONDS);
			if (obj == null) {
				throw new TimeoutException();
			}
			return obj;
		}

	}
}
