package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.sun.istack.internal.logging.Logger;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import org.fxmisc.richtext.InlineStyleTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.NavigationActions;
import tailminuseff.FileExectutor;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.FileMonitorFactory;
import tailminuseff.io.FileMonitorListener;
import tailminuseff.io.FileResetEvent;
import tailminuseff.io.LineAddedEvent;

public class FileViewController implements Initializable {

    @FXML
    private VBox textContainer;

    private final InlineStyleTextArea<InlineTextStyle> textArea;
    private final ExecutorService executorService;
    private final FileMonitor fileMonitor;
    private final EventBus eventBus;
    private final File file;
    private Future<Void> future;

    @Inject
    FileViewController(@Assisted File file, FileMonitorFactory fileMonitorFactory, @FileExectutor ExecutorService executorService, EventBus eventBus) {
        this.fileMonitor = fileMonitorFactory.createForFile(file);
        this.fileMonitor.addListener(monitorListener);
        this.executorService = executorService;
        this.eventBus = eventBus;
        this.file = file;
        this.textArea = new InlineStyleTextArea<>(new InlineTextStyle(), (style) -> style.toCSS());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textContainer.getChildren().add(this.textArea);
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        textArea.setWrapText(false);
        future = executorService.submit(fileMonitor);
    }

    public void closed() {
        future.cancel(true);
        Logger.getLogger(this.getClass()).finer("Closed {0}", new Object[]{file});
    }

    private final FileMonitorListener monitorListener = new FileMonitorListener() {
        @Override
        public void fileReset(FileResetEvent evt) {
            Platform.runLater(() -> textArea.clear());
        }

        @Override
        public void lineRead(LineAddedEvent evt) {
            Platform.runLater(() -> {
                textArea.appendText(textArea.getLength() > 0 ? "\n" + evt.getLine() : evt.getLine());
                textArea.lineStart(NavigationActions.SelectionPolicy.CLEAR);
            });
        }
    };

    private static class InlineTextStyle {

        public String toCSS() {
            return "";
        }
    }
}
