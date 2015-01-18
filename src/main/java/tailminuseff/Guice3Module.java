package tailminuseff;

import tailminuseff.config.*;
import tailminuseff.ui.MainFrame;
import tailminuseff.ui.actions.OpenFileAction;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Guice3Module extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(MainFrame.class);
		bind(OpenFileAction.class);
		bind(Configuration.class).toProvider(ConfigurationFactory.class);
		install(new FactoryModuleBuilder().implement(FileMonitor.class, SimpleFileMonitor.class).build(FileMonitorFactory.class));
	}
}
