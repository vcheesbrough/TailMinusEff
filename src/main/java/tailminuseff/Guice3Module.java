package tailminuseff;

import java.util.concurrent.*;

import tailminuseff.config.*;
import tailminuseff.ui.MainFrame;
import tailminuseff.ui.actions.OpenFileAction;

import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Guice3Module extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(MainFrame.class);
		bind(OpenFileAction.class);
		install(new FactoryModuleBuilder().implement(FileMonitor.class, SimpleFileMonitor.class).build(FileMonitorFactory.class));
	}

	@Provides
	@Singleton
	Configuration providesConfiguration(ConfigurationFactory configFactory) {
		return configFactory.readConfiguration();
	}

	@Provides
	@Singleton
	ScheduledExecutorService providesScheduledExecutorService(ApplicationExecutors applicationExecutors) {
		return applicationExecutors.createScheduledExecutorService();
	}

	@Provides
	@Singleton
	@FileExectutor
	ExecutorService providesFileExecutorService(ApplicationExecutors applicationExecutors) {
		return applicationExecutors.createFilesExecutorService();
	}

	@Provides
	@Singleton
	@GeneralExecutor
	ExecutorService providesGeneralExecutorService(ApplicationExecutors applicationExecutors) {
		return applicationExecutors.createGeneralExecutorService();
	}
}
