package tailminuseff.io;

public interface FileMonitorListener extends java.util.EventListener {

    void fileReset(FileResetEvent evt);

    void lineRead(LineAddedEvent evt);

}
