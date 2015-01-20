package tailminuseff;

import java.util.concurrent.*;

import tailminuseff.config.*;

import com.google.common.eventbus.EventBus;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Guice3Module extends AbstractModule {

	@Override
	protected void configure() {
		// bind(MainFrame.class);
		// bind(OpenFileAction.class);
		install(new FactoryModuleBuilder().implement(FileMonitor.class, SimpleFileMonitor.class).build(FileMonitorFactory.class));
	}

	@Provides
	@Singleton
	EventBus providesEventBus() {
		return new EventBus();
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
