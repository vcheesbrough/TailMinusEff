package tailminuseff.fx.actions;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javax.inject.Singleton;
import org.controlsfx.control.action.Action;

@Singleton
public class ExitAction extends Action {

    public ExitAction() {
        super("Exit");
        setEventHandler(this::exec);
    }

    public void exec(ActionEvent evt) {
        Platform.exit();
    }
}
