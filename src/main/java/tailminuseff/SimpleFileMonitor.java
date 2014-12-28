package tailminuseff;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SimpleFileMonitor implements Callable<Void> {

    private final List<FileMonitorListener> listeners = new ArrayList<FileMonitorListener>();

    private final File file;

    public SimpleFileMonitor(File file) {
        this.file = file;
    }

    public void addListener(FileMonitorListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FileMonitorListener listener) {
        listeners.remove(listener);
    }

    protected void invokeListeners(String line) {
        final FileMonitorEvent evt = new FileMonitorEvent(this, line);
        for (final FileMonitorListener listener : listeners) {
            listener.lineRead(evt);
        }
    }

    @Override
    public Void call() throws FileNotFoundException, IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {
            String line = reader.readLine();
            while (!Thread.currentThread().isInterrupted()) {
                if (line != null) {
                    invokeListeners(line);
                } else {
                    Thread.sleep(100);
                }
                line = reader.readLine();
            }
        }
        return null;
    }
}
