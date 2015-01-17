package tailminuseff;

import tailminuseff.config.*;
import tailminuseff.ui.MainFrame;

import com.google.inject.*;

public class Guice3Module extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(MainFrame.class);
	}

	@Provides
	public Configuration getConfiguration() {
		return new ConfigurationFactory(new ConfigurationIO(ConfigurationFactory.DEFAULT_FILE)).createConfiguration();
	}

	private static Injector injector;

	public synchronized static Injector getInjector() {
		if (injector == null) {
			injector = Guice.createInjector(new Guice3Module());

		}
		return injector;
	}
}
