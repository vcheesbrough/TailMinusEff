package tailminuseff.ui;

import javax.swing.SwingUtilities;
import javax.swing.text.*;

import tailminuseff.*;

public class FileLineModelDocumentAdaptor {

	private final Document document;
	private final FileLineModel fileLineModel;

	public FileLineModelDocumentAdaptor(Document document, FileLineModel fileLineModel) {
		super();
		if (!fileLineModel.getLines().isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.document = document;
		this.fileLineModel = fileLineModel;
		this.fileLineModel.addListener(modelListener);
	}

	private final FileLineModelListener modelListener = new FileLineModelListener() {

		@Override
		public void reset(FileLineModelResetEvent evt) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void lineAdded(FileLineModelLineAddedEvent evt) {
			try {
				document.insertString(document.getLength(), evt.getLine(), null);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}
	};
}
