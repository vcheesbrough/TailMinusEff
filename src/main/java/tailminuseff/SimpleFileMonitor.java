package tailminuseff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SimpleFileMonitor {

	private final List<FileMonitorListener> listeners = new ArrayList<FileMonitorListener>();

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

	public Callable<Void> GetFileMonitorCallable(File file) {
		return new FileMonitorCallable(file);
	}

	private class FileMonitorCallable implements Callable<Void> {
		private final File file;

		public FileMonitorCallable(File file) {
			this.file = file;
		}

		@Override
		public Void call() throws FileNotFoundException, IOException {
			try (BufferedReader reader = new BufferedReader(
					new FileReader(file))) {
				String line = reader.readLine();
				while (line != null) {
					invokeListeners(line);
					line = reader.readLine();
				}
			}
			return null;
		}
	}
}
