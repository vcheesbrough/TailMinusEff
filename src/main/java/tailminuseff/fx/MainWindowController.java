package tailminuseff.fx;

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

public class MainWindowController implements Initializable {

    private final Configuration config;
    private final FileViewControllerFactory fileViewFactory;
    private final ExitAction exitAction;

    @FXML
    private MenuItem exitMenuItem;

    @Inject
    public MainWindowController(Configuration config, FileViewControllerFactory fileViewFactory, ExitAction exitAction) {
        this.config = config;
        this.fileViewFactory = fileViewFactory;
        this.exitAction = exitAction;
    }

    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ActionUtils.configureMenuItem(exitAction, exitMenuItem);
        this.config.getOpenFiles().forEach(f -> OpenFile(f));
    }

    public void OpenFile(File newFile) {
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
