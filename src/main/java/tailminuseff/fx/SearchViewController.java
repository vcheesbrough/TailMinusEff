package tailminuseff.fx;

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
import tailminuseff.fx.model.InlineTextStyle;
import tailminuseff.fx.model.UserSearchModel;

public class SearchViewController implements Initializable {

    @FXML
    private CustomTextField searchText;
    @FXML
    private Button nextMatchButton;
    @FXML
    private Button prevMatchButton;

    private final PseudoClass invalidRegexPseudoClass;
    private final UserSearchModel searchModel;

    @Inject
    public SearchViewController(UserSearchModel searchModel) {
        invalidRegexPseudoClass = PseudoClass.getPseudoClass("invalid-regex");
        this.searchModel = searchModel;
        this.searchModel.setStyle(new InlineTextStyle("red", "yellow"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchText.textProperty().addListener((ignored, oldValue, newValue) -> {
            if ("".equals(newValue)) {
                searchModel.setPattern(null);
            } else {
                attemptNotifyRegex(newValue);
            }
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
            searchModel.setPattern(Pattern.compile(newValue));
            searchText.pseudoClassStateChanged(invalidRegexPseudoClass, false);
            searchText.setTooltip(null);
        } catch (PatternSyntaxException ex) {
            searchModel.setPattern(null);
            searchText.setTooltip(new Tooltip(ex.getMessage()));
            searchText.pseudoClassStateChanged(invalidRegexPseudoClass, true);
        }
    }

}
