package tailminuseff.config;

import java.io.*;

import javax.inject.Provider;

import mockit.*;

import org.junit.*;

import com.google.common.eventbus.EventBus;

public class ConfigurationFactoryTests {

	@Mocked
	private ConfigurationIO configurationIO;

	@Mocked
	Provider<PropertyChangeEventDebouncer> mockDebouncerProvider;

	private ConfigurationFactory target;

	@Before
	public void Setup() {
		target = new ConfigurationFactory(configurationIO, mockDebouncerProvider, new EventBus(this.getClass().getName()));
	}

	@Test
	public void configurationIOThrowsFileNotFoundExceptionIsIgnored(@Mocked PropertyChangeEventDebouncer mockDebouncer) throws IOException, ClassNotFoundException {
		new Expectations() {
			{
				configurationIO.readIntoFromDefaultFile((Configuration) any);
				result = new FileNotFoundException();
				mockDebouncerProvider.get();
				result = mockDebouncer;
			}
		};
		target.readConfiguration();
	}
}
