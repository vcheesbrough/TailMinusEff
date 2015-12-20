package tailminuseff.fx.actions;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;

public class ExitAction extends Action {

    public ExitAction() {
        super("Exit");
        setEventHandler(this::exec);
    }

    public void exec(ActionEvent evt) {
        Platform.exit();
    }
}
