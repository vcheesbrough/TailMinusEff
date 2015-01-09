package tailminuseff.ui;

import java.util.EventObject;

import tailminuseff.FileLineModel;

public class FileOpenedEvent extends EventObject {

	private final FileLineModel fileLineModel;

	private static final long serialVersionUID = 6431208467676467535L;

	public FileOpenedEvent(MultiFileModelSwingAdaptor source, FileLineModel model) {
		super(source);
		fileLineModel = model;
	}

	public FileLineModel getFileLineModel() {
		return fileLineModel;
	}

}
