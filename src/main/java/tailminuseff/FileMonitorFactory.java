package tailminuseff;

import java.io.File;

public final class FileMonitorFactory {
	public static FileMonitor createForFile(File file) {
		return new SimpleFileMonitor(file);
	}
}
