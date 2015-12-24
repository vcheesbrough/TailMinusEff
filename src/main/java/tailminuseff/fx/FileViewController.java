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
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import org.fxmisc.richtext.InlineStyleTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.NavigationActions;
import tailminuseff.FileExectutor;
import tailminuseff.fx.model.InlineTextStyle;
import tailminuseff.fx.model.StyledLineModel;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.FileMonitorFactory;

public class FileViewController implements Initializable {

    @FXML
    private BorderPane textContainer;

    private final InlineStyleTextArea<InlineTextStyle> textArea = new InlineStyleTextArea<>(new InlineTextStyle(), (style) -> style.toCSS());
    private final ExecutorService executorService;
    private final FileMonitor fileMonitor;
    private final EventBus eventBus;
    private final File file;
    private Future<Void> future;
    private final StyledLineModel model = new StyledLineModel();

    @Inject
    FileViewController(@Assisted File file, FileMonitorFactory fileMonitorFactory, @FileExectutor ExecutorService executorService, EventBus eventBus) {
        this.fileMonitor = fileMonitorFactory.createForFile(file);
        this.fileMonitor.addListener(model.getMonitorListener());
        this.executorService = executorService;
        this.eventBus = eventBus;
        this.file = file;
        this.model.linesProperty().addListener(linesListener);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textContainer.setCenter(this.textArea);
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        textArea.setWrapText(false);
        textArea.setEditable(false);
        textArea.autosize();
        future = executorService.submit(fileMonitor);
    }

    public void closed() {
        future.cancel(true);
        Logger.getLogger(this.getClass()).finer("Closed {0}", new Object[]{file});
    }

    private final ListChangeListener<String> linesListener = (ListChangeListener.Change<? extends String> change) -> {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().stream().forEach((line) -> {
                    Platform.runLater(() -> {
                        textArea.appendText(textArea.getLength() > 0 ? "\n" + line : line);
                        textArea.lineStart(NavigationActions.SelectionPolicy.CLEAR);
                    });
                });
            } else if (change.wasRemoved()) {
                Platform.runLater(() -> textArea.clear());
            } else {
                throw new UnsupportedOperationException();
            }
        }
    };
}
