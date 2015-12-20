package tailminuseff.fx;

import java.io.File;

public interface FileViewControllerFactory {
    
    public FileViewController createForFile(File file);
}
