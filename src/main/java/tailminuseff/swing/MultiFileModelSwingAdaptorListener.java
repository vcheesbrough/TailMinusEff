package tailminuseff.swing;

import java.util.EventListener;

public interface MultiFileModelSwingAdaptorListener extends EventListener {

    void fileOpened(FileOpenedEvent evt);

    void fileClosed(FileClosedEvent evt);
}
