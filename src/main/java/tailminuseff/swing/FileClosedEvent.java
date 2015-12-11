package tailminuseff.swing;

import java.util.EventObject;
import tailminuseff.FileLineModel;

public class FileClosedEvent extends EventObject {

    public FileClosedEvent(MultiFileModelSwingAdaptor source, FileLineModel model) {
        super(source);
        this.fileLineModel = model;
    }

    public FileLineModel getFileLineModel() {
        return fileLineModel;
    }

    private static final long serialVersionUID = -8845833943581961017L;

    private final FileLineModel fileLineModel;

}
