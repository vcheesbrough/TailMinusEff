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
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		final PropertyChangeEventDebouncer debouncer = new PropertyChangeEventDebouncer();
		c.addPropertyChangeListener(debouncer.getInputListener());
		debouncer.addPropertyChangeListener(configurationListener);
		return c;
	}

	private Configuration configuration;

	private final PropertyChangeListener configurationListener = evt -> {
		try {
			ConfigurationIO.writeToDefaultFile(configuration);
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
