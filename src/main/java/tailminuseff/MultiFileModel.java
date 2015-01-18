package tailminuseff;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import javax.inject.Inject;

public class MultiFileModel {
	private final Map<FileMonitor, Future<Void>> futuresByMonitor = new HashMap<FileMonitor, Future<Void>>();
	private final Map<File, FileLineModel> modelsByFile = new HashMap<File, FileLineModel>();
	private final List<File> modelsInOrder = new ArrayList<File>();
	private final FileMonitorFactory fileMonitorFactory;

	@Inject
	public MultiFileModel(FileMonitorFactory factory) {
		this.fileMonitorFactory = factory;
	}

	public synchronized FileLineModel openFile(File newFile) {
		final FileMonitor monitor = fileMonitorFactory.createForFile(newFile);
		final FileLineModel model = new FileLineModel(monitor);
		final Future<Void> future = ApplicationExecutors.getFilesExecutorService().submit(model.getFileMonitor());
		futuresByMonitor.put(model.getFileMonitor(), future);
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
