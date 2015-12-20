package tailminuseff.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javax.inject.Inject;
import tailminuseff.io.FileMonitor;

public class FileViewController implements Initializable {

    @FXML
    private TextArea text;

    @Inject
    FileViewController(FileMonitor fileMonitor) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
