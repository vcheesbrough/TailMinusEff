package tailminuseff.fx.actions;

import com.google.common.eventbus.EventBus;
import java.io.File;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.controlsfx.control.action.Action;
import tailminuseff.config.Configuration;
import tailminuseff.fx.FileOpenCommand;

@Singleton
public class OpenAction extends Action {

    private final Stage stage;
    private final EventBus eventBus;
    private final Configuration config;

    @Inject
    public OpenAction(Stage stage, EventBus eventBus, Configuration config) {
        super("Open …");
        setEventHandler(this::exec);
        this.stage = stage;
        this.eventBus = eventBus;
        this.config = config;
    }

    public void exec(ActionEvent evt) {
        FileChooser fileChooser = new FileChooser();
        if (config.getOpenDialogDirectory().exists()) {
            fileChooser.setInitialDirectory(config.getOpenDialogDirectory());
        }
        fileChooser.setTitle("Open Files");
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files != null && files.size() > 0) {
            config.setOpenDialogDirectory(files.get(0).getParentFile());
            files.forEach(f -> eventBus.post(new FileOpenCommand(f)));
        }
    }
}
