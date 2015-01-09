package tailminuseff.ui;

import java.util.EventListener;

public interface MultiFileModelSwingAdaptorListener extends EventListener {
	void fileOpened(FileOpenedEvent evt);
}
