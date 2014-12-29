package tailminuseff;

public interface FileMonitor {

	public abstract void addListener(FileMonitorListener listener);

	public abstract void removeListener(FileMonitorListener listener);

}