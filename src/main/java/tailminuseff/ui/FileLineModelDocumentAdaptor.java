package tailminuseff.ui;

import javax.swing.text.*;
import tailminuseff.*;

public class FileLineModelDocumentAdaptor {

    private final Document document;
    private final FileLineModel fileLineModel;

    private final FileLineModelListener modelListener = new FileLineModelListener() {

        @Override
        public void lineAdded(FileLineModelLineAddedEvent evt) {
            appendLineToDocument(evt.getLine());
        }

        @Override
        public void reset(FileLineModelResetEvent evt) {
            try {

                document.remove(0, document.getLength());
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public FileLineModelDocumentAdaptor(Document document, FileLineModel fileLineModel) {
        super();
        this.document = document;
        this.fileLineModel = fileLineModel;

        this.fileLineModel.doInLock(() -> {
            this.fileLineModel.getLines().forEach(this::appendLineToDocument);
            this.fileLineModel.addListener(modelListener);
        });
    }

    private void appendLineToDocument(String line) {
        try {
            document.insertString(document.getLength(), line + "\n", null);
        } catch (final BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
