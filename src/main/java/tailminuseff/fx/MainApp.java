package tailminuseff.fx;

import com.google.inject.Injector;
import java.awt.Rectangle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tailminuseff.Guice3Module;
import tailminuseff.StackTraceDumpingEventBusConsumer;
import tailminuseff.config.Configuration;
import static javafx.application.Application.launch;


public class MainApp extends Application {

    private ConfigMainWindowBoundsBinder configBoundsBinder = null;

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = Guice3Module.CreateInjector(stage);

        injector.getInstance(StackTraceDumpingEventBusConsumer.class);
        injector.getInstance(UnhandledExceptionDialogReceiver.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        loader.setControllerFactory(injector::getInstance);

        Parent root = loader.<Parent>load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/fx-styles.css");

        stage.setTitle("TailMinusEff");
        stage.setScene(scene);
        
        configBoundsBinder = new ConfigMainWindowBoundsBinder(injector.getInstance(Configuration.class), stage);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (configBoundsBinder != null) {
            configBoundsBinder.Unbind();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private class ConfigMainWindowBoundsBinder {

        private final Configuration config;
        private final Stage stage;

        private final ChangeListener<Number> listener;

        public ConfigMainWindowBoundsBinder(Configuration config, Stage stage) {
            this.config = config;
            this.stage = stage;
            listener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                Rectangle r = new Rectangle((int) stage.getX(), (int) stage.getY(), (int) stage.getWidth(), (int) stage.getHeight());
                config.setMainWindowBounds(r);
            };
            stage.setWidth(config.getMainWindowBounds().width);
            stage.setHeight(config.getMainWindowBounds().height);
            stage.setX(config.getMainWindowBounds().x);
            stage.setY(config.getMainWindowBounds().y);

            stage.xProperty().addListener(listener);
            stage.yProperty().addListener(listener);
            stage.widthProperty().addListener(listener);
            stage.heightProperty().addListener(listener);

        }

        public void Unbind() {
            stage.xProperty().removeListener(listener);
            stage.yProperty().removeListener(listener);
            stage.widthProperty().removeListener(listener);
            stage.heightProperty().removeListener(listener);
        }
    }

}
