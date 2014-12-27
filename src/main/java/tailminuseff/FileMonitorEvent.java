package tailminuseff;

import java.util.EventObject;

public class FileMonitorEvent extends EventObject {

	private static final long serialVersionUID = -1547478097032318524L;
	private final String line;

	public FileMonitorEvent(Object source, String line) {
		super(source);
		this.line = line;
	}

	public String getLine() {
		return line;
	}

}
