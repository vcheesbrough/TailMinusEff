package tailminuseff.io;

import eventutil.*;
import java.io.File;
import java.util.concurrent.Callable;

public abstract class FileMonitor implements Callable<Void>, EventProducer<FileMonitorListener> {

    private final EventListenerList<FileMonitorListener> listeners = new EventListenerList<FileMonitorListener>();
    private final File file;

    private boolean lastEventWasLine = true;

    public FileMonitor(File file) {
        this.file = file;
    }

    @Override
    public void addListener(FileMonitorListener listener) {
        listeners.addListener(listener);
    }

    public File getFile() {
        return file;
    }

    protected void invokeListenersWithAdded(String line) {
        lastEventWasLine = true;
        final LineAddedEvent evt = new LineAddedEvent(this, line);
        listeners.forEachLisener(listener -> listener.lineRead(evt));
    }

    protected void invokeListenersWithReset() {
        if (lastEventWasLine) {
            final FileResetEvent evt = new FileResetEvent(this);
            listeners.forEachLisener(listener -> listener.fileReset(evt));
        }
        lastEventWasLine = false;
    }

    @Override
    public void removeListener(FileMonitorListener listener) {
        listeners.removeListener(listener);
    }

}
