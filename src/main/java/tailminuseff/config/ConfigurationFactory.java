package tailminuseff.config;

import java.beans.PropertyChangeListener;
import java.io.*;

public class ConfigurationFactory {
	public synchronized Configuration getConfiguration() {
		if (configuration == null) {
			configuration = createConfiguration();
		}
		return configuration;
	}

	private Configuration createConfiguration() {
		final Configuration c = new Configuration();
		try {
			ConfigurationIO.readIntoFromDefaultFile(c);
		} catch (final FileNotFoundException fnfex) {
			// fine ignore this
		} catch (final IOException ioex) {
			throw new RuntimeException(ioex);
		}
		c.addPropertyChangeListener(configurationListener);
		return c;
	}

	private Configuration configuration;

	private final PropertyChangeListener configurationListener = evt -> {
		final Configuration c = (Configuration) evt.getSource();
		try {
			ConfigurationIO.writeToDefaultFile(c);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	private static ConfigurationFactory instance;

	public synchronized static ConfigurationFactory getInstance() {
		if (instance == null) {
			instance = new ConfigurationFactory();
		}
		return instance;
	}

	ConfigurationFactory() {
	}
}
