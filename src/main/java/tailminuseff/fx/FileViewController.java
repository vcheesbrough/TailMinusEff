package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.inject.Inject;
import org.fxmisc.richtext.CodeArea;
import tailminuseff.FileExectutor;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.FileMonitorFactory;
import tailminuseff.io.FileMonitorListener;
import tailminuseff.io.FileResetEvent;
import tailminuseff.io.LineAddedEvent;

public class FileViewController implements Initializable {

    @FXML
    private CodeArea text;

    private final ExecutorService executorService;
    private final FileMonitor fileMonitor;

    private final FileMonitorListener monitorListener = new FileMonitorListener() {
        @Override
        public void fileReset(FileResetEvent evt) {
            Platform.runLater(() -> text.clear());
        }

        @Override
        public void lineRead(LineAddedEvent evt) {
            Platform.runLater(() -> text.appendText(evt.getLine() + "\n"));
        }
    };
    private final EventBus eventBus;

    @Inject
    FileViewController(@Assisted File file, FileMonitorFactory fileMonitorFactory, @FileExectutor ExecutorService executorService, EventBus eventBus) {
        this.fileMonitor = fileMonitorFactory.createForFile(file);
        this.fileMonitor.addListener(monitorListener);
        this.executorService = executorService;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        executorService.submit(fileMonitor);
    }

}
