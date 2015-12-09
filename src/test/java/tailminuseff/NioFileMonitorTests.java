package tailminuseff;

import java.io.File;

public class NioFileMonitorTests extends FileMonitorTests<NioFileMonitor> {

    @Override
    protected NioFileMonitor createTarget(File file) {
        return new NioFileMonitor(file);
    }
}
