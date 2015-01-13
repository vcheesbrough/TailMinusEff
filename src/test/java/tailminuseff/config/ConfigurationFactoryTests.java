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
		target = new ConfigurationFactory();
	}

	@Test
	public void getConfigurationReturnsSameEveryTime() {
		assertEquals(target.getConfiguration(), target.getConfiguration());
	}

	@Test
	public void configurationIOThrowsFileNotFoundExceptionIsIgnored() throws IOException {
		new Expectations() {
			{
				ConfigurationIO.readIntoFromDefaultFile((Configuration) any);
				result = new FileNotFoundException();
			}
		};
		target.getConfiguration();
	}

	@Test
	public void getInstanceReturnsSameInstanceEveryTime() {
		assertEquals(ConfigurationFactory.getInstance(), ConfigurationFactory.getInstance());
	}
}
