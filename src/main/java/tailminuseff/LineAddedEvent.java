package tailminuseff;

import java.util.EventObject;

public class LineAddedEvent extends EventObject {

    private static final long serialVersionUID = -1547478097032318524L;
    private final String line;

    public LineAddedEvent(Object source, String line) {
        super(source);
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return super.toString() + " line=\"" + getLine() + "\"";
    }
}
