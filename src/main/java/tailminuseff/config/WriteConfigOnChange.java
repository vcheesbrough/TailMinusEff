package tailminuseff.config;

import java.beans.PropertyChangeListener;
import java.io.IOException;

public class WriteConfigOnChange {

	private ConfigurationIO configIO;
	private Configuration config;

	public WriteConfigOnChange(Configuration config, ConfigurationIO configIO) {
		this.config = config;
		this.configIO = configIO;
		config.addPropertyChangeListener(configurationListener);
	}

	private final PropertyChangeListener configurationListener = evt -> {
		try {
			configIO.writeToDefaultFile(config);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
}
