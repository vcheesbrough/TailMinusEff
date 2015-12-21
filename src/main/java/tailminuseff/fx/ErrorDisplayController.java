package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.controlsfx.dialog.ExceptionDialog;
import tailminuseff.UnhandledException;

@Singleton
public class ErrorDisplayController {

    @Inject
    public ErrorDisplayController(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void receiveException(UnhandledException error) {
        Platform.runLater(() -> {
            ExceptionDialog dlg = new ExceptionDialog(error.getThrown());
            dlg.showAndWait();
        });
    }
}
