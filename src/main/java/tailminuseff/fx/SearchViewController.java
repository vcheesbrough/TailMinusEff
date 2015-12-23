package tailminuseff.fx;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javax.inject.Inject;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchViewController implements Initializable {

    @FXML
    private CustomTextField searchText;
    @FXML
    private Button nextMatchButton;
    @FXML
    private Button prevMatchButton;

    @Inject
    public SearchViewController(EventBus eventBus) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

}
