package tailminuseff;

import java.io.File;

public interface FileMonitorFactory {

    FileMonitor createForFile(File file);
}
