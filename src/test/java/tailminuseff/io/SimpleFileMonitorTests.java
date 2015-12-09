package tailminuseff.io;

import tailminuseff.io.SimpleFileMonitor;
import java.io.File;
import org.junit.Ignore;

@Ignore
public class SimpleFileMonitorTests extends FileMonitorTests<SimpleFileMonitor> {

    @Override
    protected SimpleFileMonitor createTarget(File file) {
        return new SimpleFileMonitor(file);
    }
}
