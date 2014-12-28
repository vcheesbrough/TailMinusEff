package tailminuseff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleFileMonitor implements Callable<Void> {

    private static final Pattern linePattern = Pattern.compile(".*?\r?\n", Pattern.MULTILINE | Pattern.DOTALL);
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
        StringBuilder stringBuilder = new StringBuilder();

        try (FileReader reader = new FileReader(file)) {
            while (!Thread.currentThread().isInterrupted()) {
                char buffer[] = new char[8192];
                int bytesRead = reader.read(buffer);
                if (bytesRead > 0) {
                    stringBuilder.append(buffer, 0, bytesRead);
                    Matcher matcher = linePattern.matcher(stringBuilder);
                    while (matcher.find()) {
                        String line = stringBuilder.substring(matcher.start(), matcher.end());
                        invokeListeners(line);
                        stringBuilder.delete(0, matcher.end());
                        matcher.reset(stringBuilder);
                    }
                } else if (bytesRead == -1) {
                    Thread.sleep(100);
                }
            }
        }
        return null;
    }
}
