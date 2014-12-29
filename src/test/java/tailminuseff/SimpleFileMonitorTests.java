package tailminuseff;

import java.io.File;

public class SimpleFileMonitorTests extends FileMonitorTests<SimpleFileMonitor> {

	@Override
	protected SimpleFileMonitor createTarget(File file) {
		return new SimpleFileMonitor(file);
	}
}
