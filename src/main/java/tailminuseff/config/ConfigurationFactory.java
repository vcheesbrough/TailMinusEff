package tailminuseff.config;

import java.io.*;

import javax.inject.*;

import tailminuseff.UnhandledException;

import com.google.common.eventbus.EventBus;

@Singleton
public class ConfigurationFactory implements Provider<Configuration> {
	private final ConfigurationIO configurationIO;
	private final Provider<PropertyChangeEventDebouncer> debouncerProvider;
	private final EventBus eventBus;

	@Inject
	public ConfigurationFactory(ConfigurationIO configIO, Provider<PropertyChangeEventDebouncer> debouncerProvider, EventBus eventBus) {
		this.configurationIO = configIO;
		this.debouncerProvider = debouncerProvider;
		this.eventBus = eventBus;
	}

	public Configuration readConfiguration() {
		final Configuration c = new Configuration();
		try {
			configurationIO.readIntoFromDefaultFile(c);
		} catch (final FileNotFoundException fnfex) {
			// fine ignore this
		} catch (IOException | ClassNotFoundException ex) {
			eventBus.post(new UnhandledException(ex, "Unable to read configuration from \"" + configurationIO.getFile().getAbsolutePath() + "\""));
		}
		final PropertyChangeEventDebouncer debouncer = debouncerProvider.get();
		c.addPropertyChangeListener(debouncer.getInputListener());
		debouncer.addPropertyChangeListener(new WriteConfigOnChange(c, configurationIO, eventBus));
		return c;
	}

	@Override
	public Configuration get() {
		return readConfiguration();
	}
}
