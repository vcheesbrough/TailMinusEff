package tailminuseff.config;

import java.beans.PropertyChangeListener;
import java.io.*;

import javax.inject.Singleton;

@Singleton
public class ConfigurationFactory {
	public static final File DEFAULT_FILE = new File(System.getProperty("user.home"), ".tailminuseff.config");

	private final ConfigurationIO configurationIO;

	public ConfigurationFactory(ConfigurationIO configIO) {
		this.configurationIO = configIO;
	}

	public Configuration createConfiguration() {
		final Configuration c = new Configuration();
		try {
			configurationIO.readIntoFromDefaultFile(c);
		} catch (final FileNotFoundException fnfex) {
			// fine ignore this
		} catch (IOException | ClassNotFoundException ioex) {
			ioex.printStackTrace();
		}
		final PropertyChangeEventDebouncer debouncer = new PropertyChangeEventDebouncer();
		c.addPropertyChangeListener(debouncer.getInputListener());
		debouncer.addPropertyChangeListener(configurationListener);
		return c;
	}

	private Configuration configuration;

	private final PropertyChangeListener configurationListener = evt -> {
		try {
			configurationIO.writeToDefaultFile(configuration);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
}
