package tailminuseff.config;

import java.io.*;

import javax.inject.*;

@Singleton
public class ConfigurationFactory implements Provider<Configuration> {
	private final ConfigurationIO configurationIO;
	private final Provider<PropertyChangeEventDebouncer> debouncerProvider;

	@Inject
	public ConfigurationFactory(ConfigurationIO configIO, Provider<PropertyChangeEventDebouncer> debouncerProvider) {
		this.configurationIO = configIO;
		this.debouncerProvider = debouncerProvider;
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
		final PropertyChangeEventDebouncer debouncer = debouncerProvider.get();
		c.addPropertyChangeListener(debouncer.getInputListener());
		debouncer.addPropertyChangeListener(new WriteConfigOnChange(c, configurationIO));
		return c;
	}

	@Override
	public Configuration get() {
		return readConfiguration();
	}
}
