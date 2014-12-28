package tailminuseff;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleFileMonitorTests {

    private File file;
    private SimpleFileMonitor target;
    @Mocked
    private FileMonitorListener mockListener;
    private TestListener testListener;
    private ExecutorService executorService;

    @Before
    public void Setup() throws IOException {
        file = File.createTempFile("SimpleFileMonitorTests", "");
        file.deleteOnExit();
        target = new SimpleFileMonitor();
        target.addListener(mockListener);
        testListener = new TestListener();
        target.addListener(testListener);
        executorService = Executors.newCachedThreadPool();
    }

    @After
    public void Teardown() throws Exception {
        if (this.executorService != null) {
            this.executorService.shutdown();
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
                mockListener.lineRead((FileMonitorEvent) any);
                times = 1;
            }
        };
        this.executorService.submit(target.GetFileMonitorCallable(file));
        this.testListener.GetNextEvent();
    }

    @Test
    public void singleLineFileCallsListenerWithCorrectEventSource()
            throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\n");

        this.executorService.submit(target.GetFileMonitorCallable(file));

        assertEquals(target, testListener.GetNextEvent().getSource());
    }

    @Test
    public void singleLineFileCallsListenerWithCorrectLine() throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\n");

        this.executorService.submit(target.GetFileMonitorCallable(file));

        assertEquals("Hello World", testListener.GetNextEvent().getLine());
    }

    @Test
    public void twoLineFileCallsListenerWithCorrectLine() throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\nAnother Line\n");

        this.executorService.submit(target.GetFileMonitorCallable(file));

        assertEquals("Hello World", testListener.GetNextEvent().getLine());
        assertEquals("Another Line", testListener.GetNextEvent().getLine());
    }

    @Test
    public void lineAddedToFileCallsListenerOnce() throws Exception {
        Files.write(file.toPath(), "Hello World\n".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target.GetFileMonitorCallable(file));

        assertEquals("Hello World", testListener.GetNextEvent().getLine());

        Files.write(file.toPath(), "Another Line\n".getBytes(), StandardOpenOption.APPEND);

        assertEquals("Another Line", testListener.GetNextEvent().getLine());
    }

    @Test
    public void lotsOflinesAddedToFileCallsListenerMultipleTimes() throws Exception {
        Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target.GetFileMonitorCallable(file));

        assertEquals("FirstLine", testListener.GetNextEvent().getLine());

        final int lineCount = 1000;

        Future<Void> writer = this.executorService.submit(new Callable<Void>() {
            public Void call() throws IOException {
                try (OutputStream writer = new FileOutputStream(file, true)) {
                    for (int i = 0; i < lineCount; i++) {
                        writer.write(("Another Line " + i + "\n").getBytes());
                    }
                }
                return null;
            }
        });
        for (int i = 0; i < lineCount; i++) {
            assertEquals("Another Line " + i, testListener.GetNextEvent().getLine());
        }
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
            assertNotNull("No event appeared within timout", evt);
            return evt;
        }
    }
}
