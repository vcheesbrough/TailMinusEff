package tailminuseff;

import java.util.EventObject;

public class FileLineModelLineAddedEvent extends EventObject {

	private static final long serialVersionUID = 7548064943663260647L;
	private final String line;

	public FileLineModelLineAddedEvent(FileLineModel source, String line) {
		super(source);
		this.line = line;
	}

	public String getLine() {
		return this.line;
	}

}
