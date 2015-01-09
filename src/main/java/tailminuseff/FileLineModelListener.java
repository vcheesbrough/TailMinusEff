package tailminuseff;

import java.util.EventListener;

public interface FileLineModelListener extends EventListener {
	void lineAdded(FileLineModelLineAddedEvent evt);

	void reset(FileLineModelResetEvent evt);
}
