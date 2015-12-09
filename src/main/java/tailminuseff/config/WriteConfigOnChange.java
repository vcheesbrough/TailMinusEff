package tailminuseff.config;

import com.google.common.eventbus.EventBus;
import java.beans.*;
import java.io.IOException;
import tailminuseff.UnhandledException;

public class WriteConfigOnChange implements PropertyChangeListener {

    private final ConfigurationIO configIO;
    private final Configuration config;
    private final EventBus eventBus;

    public WriteConfigOnChange(Configuration config, ConfigurationIO configIO, EventBus eventBus) {
        this.config = config;
        this.configIO = configIO;
        this.eventBus = eventBus;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            configIO.writeToDefaultFile(config);
        } catch (final IOException e) {
            eventBus.post(new UnhandledException(e, "Unable to write config to \"" + configIO.getFile().getAbsolutePath() + "\""));
        }
    }
}
