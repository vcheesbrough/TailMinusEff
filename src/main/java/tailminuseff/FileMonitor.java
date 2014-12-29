package tailminuseff;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

public abstract class FileMonitor implements Callable<Void> {

	private final List<FileMonitorListener> listeners = new ArrayList<FileMonitorListener>();
	private final File file;

	public File getFile() {
		return file;
	}

	private boolean lastEventWasLine = true;

	public FileMonitor(File file) {
		this.file = file;
	}

	public void addListener(FileMonitorListener listener) {
		listeners.add(listener);
	}

	public void removeListener(FileMonitorListener listener) {
		listeners.remove(listener);
	}

	protected void invokeListenersWithAdded(String line) {
		lastEventWasLine = true;
		final LineAddedEvent evt = new LineAddedEvent(this, line);
		for (final FileMonitorListener listener : listeners) {
			listener.lineRead(evt);
		}
	}

	protected void invokeListenersWithReset() {
		if (lastEventWasLine) {
			final FileResetEvent evt = new FileResetEvent(this);
			for (final FileMonitorListener listener : listeners) {
				listener.fileReset(evt);
			}
		}
		lastEventWasLine = false;
	}

}