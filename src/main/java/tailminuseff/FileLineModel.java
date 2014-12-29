package tailminuseff;

import java.util.*;

public class FileLineModel {
	private final FileMonitor fileMonitor;
	private final List<String> lines = new ArrayList<String>();
	private final List<FileLineModelListener> listeners = new ArrayList<FileLineModelListener>();

	public void addListener(FileLineModelListener listener) {
		listeners.add(listener);
	}

	public FileLineModel(FileMonitor fileMonitor) {
		super();
		this.fileMonitor = fileMonitor;
		this.fileMonitor.addListener(monitorListener);
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(this.lines);
	}

	private final FileMonitorListener monitorListener = new FileMonitorListener() {

		@Override
		public void lineRead(LineAddedEvent evt) {
			lines.add(evt.getLine());
			for (final FileLineModelListener listener : listeners) {
				listener.lineAdded(new FileLineModelLineAddedEvent(FileLineModel.this, evt.getLine()));
			}
		}

		@Override
		public void fileReset(FileResetEvent evt) {
			lines.clear();
			for (final FileLineModelListener listener : listeners) {
				listener.reset(new FileLineModelResetEvent(FileLineModel.this));
			}
		}
	};
}
