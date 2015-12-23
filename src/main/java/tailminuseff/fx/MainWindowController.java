package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.inject.Inject;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.CustomTextField;
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
    private final List<File> openFiles = new ArrayList<>();

    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane tabPane;
    @FXML
    private CustomTextField searchText;
    @FXML
    private Button nextMatchButton;
    @FXML
    private Button prevMatchButton;
    @FXML
    private Button openButton;
    @FXML
    private Button exitButton;

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
        ActionUtils.configureButton(openAction, openButton);
        ActionUtils.configureButton(exitAction, exitButton);

        if (this.config.getOpenFiles().size() > 0) {
            this.config.getOpenFiles().forEach(f -> openFile(f));
            if (this.config.getSelectedFile() != null) {
                tabPane.getTabs().stream()
                        .filter((tab) -> this.config.getSelectedFile().equals(tab.getUserData()))
                        .findFirst()
                        .ifPresent(tabPane.getSelectionModel()::select);
            }
            tabPane.getTabs().forEach((tab) -> {
                tab.selectedProperty().addListener((ignore, oldValue, tabSelected) -> {
                    if (tabSelected) {
                        config.setSelectedFile((File) tab.getUserData());
                    }
                });

            });
        } else {
            this.openAction.exec(new ActionEvent());
        }
        
    }

    @Subscribe
    public void receiveFileOpenCommand(FileOpenCommand command) {
        if (!openFiles.contains(command.getFile())) {
            openFile(command.getFile());
            config.setOpenFiles(openFiles);
        }
    }
    
    private void openFile(File newFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FILEVIEW_FXMLPATH));
            loader.setControllerFactory((Class<?> type) -> fileViewFactory.createForFile(newFile));
            final Tab tab = new Tab(newFile.getName(), loader.load());
            tab.setUserData(newFile);

            final FileViewController controller = loader.getController();

            tab.setOnClosed((evt) -> {
                closeFile(newFile);
                controller.closed();
            });

            tabPane.getTabs().add(tab);
            openFiles.add(newFile);
        } catch (IOException ex) {
            eventBus.post(new UnhandledException(ex, String.format("Could not load \"%s\"", FILEVIEW_FXMLPATH)));
        }
    }

    private void closeFile(File existingFile) {
        openFiles.remove(existingFile);
        config.setOpenFiles(openFiles);
    }

    @FXML
    private void nextMatchClicked(ActionEvent event) {
    }

    @FXML
    private void prevMatchClicked(ActionEvent event) {
    }

    @FXML
    private void closeSearchClicked(ActionEvent event) {
    }

    @FXML
    private void onActionSearchText(ActionEvent event) {
    }
}
