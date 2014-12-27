package tailminuseff;

public interface FileMonitorListener extends java.util.EventListener {

	void lineRead(FileMonitorEvent evt);

}
