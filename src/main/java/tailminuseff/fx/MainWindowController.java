package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.inject.Inject;
import org.controlsfx.control.action.ActionUtils;
import tailminuseff.config.Configuration;
import tailminuseff.fx.actions.ExitAction;
import tailminuseff.fx.actions.OpenAction;

public class MainWindowController implements Initializable {

    private final Configuration config;
    private final FileViewControllerFactory fileViewFactory;
    private final ExitAction exitAction;
    private final OpenAction openAction;

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
        eventBus.register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FileView.fxml"));
            loader.setControllerFactory((Class<?> type) -> fileViewFactory.createForFile(newFile));
            final Tab tab = new Tab(newFile.getName(), loader.load());
            tabPane.getTabs().add(tab);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
