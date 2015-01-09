package tailminuseff;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class MultiFileModel {
	private final Map<FileMonitor, Future<Void>> futuresByMonitor = Collections.synchronizedMap(new HashMap<FileMonitor, Future<Void>>());

	public void close(FileLineModel fileLineModel) throws InterruptedException, ExecutionException {
		final Future<Void> future = futuresByMonitor.remove(fileLineModel.getFileMonitor());
		future.cancel(true);
		future.get();
	}

	public FileLineModel openFile(File newFile) {
		final FileMonitor m = FileMonitorFactory.createForFile(newFile);
		final Future<Void> future = ApplicationExecutors.getFilesExecutorService().submit(m);
		futuresByMonitor.put(m, future);
		return new FileLineModel(m);
	}
}
