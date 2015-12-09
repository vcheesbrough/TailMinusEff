package tailminuseff;

import eventutil.*;
import java.io.File;
import java.util.*;
import javax.inject.Inject;

public class FileLineModel implements EventProducer<FileLineModelListener> {

    private final FileMonitor fileMonitor;
    private final List<String> lines = new ArrayList<String>();
    private final EventListenerList<FileLineModelListener> listeners = new EventListenerList<FileLineModelListener>(this);

    private final FileMonitorListener monitorListener = new FileMonitorListener() {

        @Override
        public void fileReset(FileResetEvent evt) {
            synchronized (FileLineModel.this) {
                lines.clear();
                listeners.forEachLisener(listener -> listener.reset(new FileLineModelResetEvent(FileLineModel.this)));
            }
        }

        @Override
        public void lineRead(LineAddedEvent evt) {
            synchronized (FileLineModel.this) {
                lines.add(evt.getLine());
                listeners.forEachLisener(listener -> listener.lineAdded(new FileLineModelLineAddedEvent(FileLineModel.this, evt.getLine())));
            }
        }
    };

    @Inject
    public FileLineModel(FileMonitor monitor) {
        super();
        this.fileMonitor = monitor;
        this.fileMonitor.addListener(monitorListener);
    }

    @Override
    public void addListener(FileLineModelListener listener) {
        listeners.addListener(listener);
    }

    public void doInLock(Runnable action) {
        synchronized (this) {
            action.run();
        }
    }

    public File getFile() {
        synchronized (this) {
            return fileMonitor.getFile();
        }
    }

    public FileMonitor getFileMonitor() {
        synchronized (this) {
            return fileMonitor;
        }
    }

    public List<String> getLines() {
        synchronized (this) {
            return Collections.unmodifiableList(this.lines);
        }
    }

    @Override
    public void removeListener(FileLineModelListener listener) {
        listeners.removeListener(listener);
    }

}
