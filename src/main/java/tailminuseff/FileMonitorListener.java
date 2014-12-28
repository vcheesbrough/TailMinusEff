package tailminuseff;

public interface FileMonitorListener extends java.util.EventListener {

    void lineRead(LineAddedEvent evt);

    void fileReset(FileResetEvent evt);

}
