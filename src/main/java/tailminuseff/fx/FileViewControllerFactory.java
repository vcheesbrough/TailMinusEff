package tailminuseff.fx;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;
import tailminuseff.io.FileMonitorFactory;

@Singleton
public class FileViewControllerFactory {
    
    private final FileMonitorFactory monitorFactory;
    
    @Inject
    public FileViewControllerFactory(FileMonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
    }
    
    public FileViewController createForFile(File file) {
        return new FileViewController(monitorFactory.createForFile(file));
    }
}
