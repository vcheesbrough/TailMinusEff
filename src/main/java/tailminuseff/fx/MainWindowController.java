package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.inject.Inject;
import org.controlsfx.control.action.ActionUtils;
import tailminuseff.UnhandledException;
import tailminuseff.config.Configuration;
import tailminuseff.fx.actions.ExitAction;
import tailminuseff.fx.actions.OpenAction;

public class MainWindowController implements Initializable {
    private static final String FILEVIEW_FXMLPATH = "/fxml/FileView.fxml";

    private final Configuration config;
    private final FileViewControllerFactory fileViewFactory;
    private final ExitAction exitAction;
    private final OpenAction openAction;
    private final EventBus eventBus;

    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane tabPane;

    @Inject
    public MainWindowController(Configuration config, FileViewControllerFactory fileViewFactory, ExitAction exitAction, OpenAction openAction, EventBus eventBus) {
        this.config = config;
        this.fileViewFactory = fileViewFactory;
        this.exitAction = exitAction;
        this.openAction = openAction;
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Mac")) {
            menuBar.useSystemMenuBarProperty().set(true);
        }
        ActionUtils.configureMenuItem(exitAction, exitMenuItem);
        ActionUtils.configureMenuItem(openAction, openMenuItem);
        this.config.getOpenFiles().forEach(f -> openFile(f));
    }

    @Subscribe
    public void receiveFileOpenCommand(FileOpenCommand command) {
        openFile(command.getFile());
    }
    
    public void openFile(File newFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FILEVIEW_FXMLPATH));
            loader.setControllerFactory((Class<?> type) -> fileViewFactory.createForFile(newFile));
            final Tab tab = new Tab(newFile.getName(), loader.load());
            tabPane.getTabs().add(tab);
        } catch (IOException ex) {
            eventBus.post(new UnhandledException(ex, String.format("Could not load \"%s\"", FILEVIEW_FXMLPATH)));
        }
    }
}
