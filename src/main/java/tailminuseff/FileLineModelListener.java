package tailminuseff;

public interface FileLineModelListener {
	void lineAdded(FileLineModelLineAddedEvent evt);

	void reset(FileLineModelResetEvent evt);
}
