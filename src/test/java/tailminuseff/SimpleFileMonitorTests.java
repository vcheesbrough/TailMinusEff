package tailminuseff;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
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
import static org.junit.Assert.fail;

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
        target = new SimpleFileMonitor(file);
        target.addListener(mockListener);
        testListener = new TestListener();
        target.addListener(testListener);
        executorService = Executors.newCachedThreadPool();
    }

    @After
    public void Teardown() throws Exception {
        if (this.executorService != null) {
            this.executorService.shutdownNow();
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
        this.executorService.submit(target);
        this.testListener.getNextEventAsLine();
    }

    @Test
    public void singleLineFileCallsListenerWithCorrectEventSource()
            throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\n");

        this.executorService.submit(target);

        assertEquals(target, testListener.getNextEventAsLine().getSource());
    }

    @Test
    public void singleLineFileCallsListenerWithCorrectLine() throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\n");

        this.executorService.submit(target);

        assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
    }

    @Test
    public void twoLineFileCallsListenerWithCorrectLine() throws Exception {
        FileUtils.writeStringToFile(file, "Hello World\nAnother Line\n");

        this.executorService.submit(target);

        assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
        assertEquals("Another Line\n", testListener.getNextEventAsLine().getLine());
    }

    @Test
    public void lineAddedToFileCallsListenerOnce() throws Exception {
        Files.write(file.toPath(), "Hello World\n".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target);

        assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());

        Files.write(file.toPath(), "Another Line\n".getBytes(), StandardOpenOption.APPEND);

        assertEquals("Another Line\n", testListener.getNextEventAsLine().getLine());
    }

    @Test
    public void windowsLineEndingsSplitLinesAlso() throws Exception {
        Files.write(file.toPath(), "Hello World\r\n".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target);

        assertEquals("Hello World\r\n", testListener.getNextEventAsLine().getLine());

    }

    @Test
    public void lineAddedInTwoPartsIsReadAsOneLine() throws Exception {
        Files.write(file.toPath(), "Hello ".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target);

        try {
            LineAddedEvent evt = testListener.getNextEventAsLine();
            fail("Did not expect event " + evt);
        } catch (TimeoutException tex) {
        }

        Files.write(file.toPath(), "World\n".getBytes(), StandardOpenOption.APPEND);
        assertEquals("Hello World\n", testListener.getNextEventAsLine().getLine());
    }

    @Test
    public void lotsOfLinesAddedToFileCallsListenerMultipleTimes() throws Exception {
        Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);

        this.executorService.submit(target);

        assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());

        final int lineCount = 100;

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
            assertEquals("Another Line " + i + "\n", testListener.getNextEventAsLine().getLine());
        }
    }

    @Test
    public void removedListenerDoesNotGetInvoked() throws Exception {
        this.executorService.submit(target);

        Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);
        assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());
        target.removeListener(testListener);

        Files.write(file.toPath(), "SecondLine\n".getBytes(), StandardOpenOption.APPEND);
        try {
            LineAddedEvent evt = testListener.getNextEventAsLine();
            fail("Did not expect event " + evt);
        } catch (TimeoutException tex) {
        }
    }

    @Test
    //@Ignore
    public void deletedFileGeneratesResetEvent() throws Exception {
        this.executorService.submit(target);

        Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);
        assertEquals("FirstLine\n", testListener.getNextEventAsLine().getLine());

        Files.delete(file.toPath());
        testListener.getNextEventAsReset();

        Files.write(file.toPath(), "ThirdLine\n".getBytes(), StandardOpenOption.CREATE_NEW);
        assertEquals("ThirdLine\n", testListener.getNextEventAsLine().getLine());

        new Verifications() {{
            mockListener.fileReset((FileResetEvent) any);
            times = 1;
        }};
    }

    private class TestListener implements FileMonitorListener {
        private final BlockingQueue<Object> event = new LinkedBlockingQueue<>();

        @Override
        public void lineRead(LineAddedEvent evt) {
            if (!event.offer(evt)) {
                throw new IllegalStateException();
            }
        }

        @Override
        public void fileReset(FileResetEvent evt) {
            if (!event.offer(evt)) {
                throw new IllegalStateException();
            }
            System.out.println("reset");
        }

        public LineAddedEvent getNextEventAsLine() throws InterruptedException, TimeoutException {
            LineAddedEvent evt = (LineAddedEvent) event.poll(200, TimeUnit.MILLISECONDS);
            if (evt == null) {
                throw new TimeoutException();
            }
            return evt;
        }

        public FileResetEvent getNextEventAsReset() throws InterruptedException, TimeoutException {
            FileResetEvent evt = (FileResetEvent) event.poll(200, TimeUnit.MILLISECONDS);
            if (evt == null) {
                throw new TimeoutException();
            }
            return evt;
        }
    }
}
