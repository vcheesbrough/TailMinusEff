package tailminuseff.config;

import java.io.*;

import javax.inject.*;

@Singleton
public class ConfigurationFactory implements Provider<Configuration> {
	private final ConfigurationIO configurationIO;

	@Inject
	public ConfigurationFactory(ConfigurationIO configIO) {
		this.configurationIO = configIO;
	}

	public Configuration readConfiguration() {
		final Configuration c = new Configuration();
		try {
			configurationIO.readIntoFromDefaultFile(c);
		} catch (final FileNotFoundException fnfex) {
			// fine ignore this
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		final PropertyChangeEventDebouncer debouncer = new PropertyChangeEventDebouncer();
		c.addPropertyChangeListener(debouncer.getInputListener());
		new WriteConfigOnChange(c, configurationIO);
		return c;
	}

	@Override
	public Configuration get() {
		return readConfiguration();
	}
}
