package tailminuseff;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileLineModelFactory {
	private final FileMonitorFactory monitorFactory;

	@Inject
	public FileLineModelFactory(FileMonitorFactory monitorFactory) {
		this.monitorFactory = monitorFactory;
	}

	public FileLineModel createForFile(File file) {
		return new FileLineModel(monitorFactory.createForFile(file));
	}
}
