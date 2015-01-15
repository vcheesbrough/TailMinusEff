package tailminuseff;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class MultiFileModel {
	private final Map<FileMonitor, Future<Void>> futuresByMonitor = new HashMap<FileMonitor, Future<Void>>();
	private final Map<File, FileLineModel> modelsByFile = new HashMap<File, FileLineModel>();
	private final List<File> modelsInOrder = new ArrayList<File>();

	public synchronized FileLineModel openFile(File newFile) {
		final FileMonitor m = FileMonitorFactory.createForFile(newFile);
		final Future<Void> future = ApplicationExecutors.getFilesExecutorService().submit(m);
		futuresByMonitor.put(m, future);
		final FileLineModel model = new FileLineModel(m);
		modelsByFile.put(newFile, model);
		modelsInOrder.add(newFile);
		return model;
	}

	public synchronized void close(FileLineModel fileLineModel) throws InterruptedException, ExecutionException {
		final Future<Void> future = futuresByMonitor.remove(fileLineModel.getFileMonitor());
		modelsByFile.remove(fileLineModel.getFile());
		modelsInOrder.remove(fileLineModel.getFile());
		future.cancel(true);
		try {
			future.get();
		} catch (final CancellationException cex) { // this is expected

		}
	}

	public synchronized FileLineModel close(File file) throws InterruptedException, ExecutionException {
		final FileLineModel fileLineModel = modelsByFile.get(file);
		close(fileLineModel);
		return fileLineModel;
	}

	public synchronized List<File> getOpenFiles() {
		return this.modelsInOrder;
	}
}
