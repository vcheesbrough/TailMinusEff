package tailminuseff.config;

import static org.junit.Assert.assertEquals;

import java.io.*;

import mockit.*;

import org.junit.*;

public class ConfigurationFactoryTests {

	@Mocked
	private ConfigurationIO configurationIO;

	private ConfigurationFactory target;

	@Before
	public void Setup() {
		target = new ConfigurationFactory(configurationIO);
	}

	@Test
	public void configurationIOThrowsFileNotFoundExceptionIsIgnored() throws IOException, ClassNotFoundException {
		new Expectations() {
			{
				configurationIO.readIntoFromDefaultFile((Configuration) any);
				result = new FileNotFoundException();
			}
		};
		target.readConfiguration();
	}
}
