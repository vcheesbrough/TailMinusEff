package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javax.inject.Inject;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchViewController implements Initializable {

    @FXML
    private CustomTextField searchText;
    @FXML
    private Button nextMatchButton;
    @FXML
    private Button prevMatchButton;
    private final EventBus eventBus;
    private final PseudoClass invalidRegexPseudoClass;

    @Inject
    public SearchViewController(EventBus eventBus) {
        invalidRegexPseudoClass = PseudoClass.getPseudoClass("invalid-regex");
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchText.textProperty().addListener((ignored, oldValue, newValue) -> {
            attemptNotifyRegex(newValue);
        });
        nextMatchButton.setVisible(false);
        prevMatchButton.setVisible(false);
    }

    @FXML
    private void nextMatchClicked(ActionEvent event) {
    }

    @FXML
    private void prevMatchClicked(ActionEvent event) {
    }

    @FXML
    private void onActionSearchText(ActionEvent event) {
    }

    private void attemptNotifyRegex(String newValue) {
        try {
            eventBus.post(new UserSearchCommand(Pattern.compile(newValue)));
            searchText.pseudoClassStateChanged(invalidRegexPseudoClass, false);
            searchText.setTooltip(null);
        } catch (PatternSyntaxException ex) {
            searchText.setTooltip(new Tooltip(ex.getMessage()));
            searchText.pseudoClassStateChanged(invalidRegexPseudoClass, true);
        }
    }

}
