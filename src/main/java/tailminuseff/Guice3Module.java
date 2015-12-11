package tailminuseff;

import tailminuseff.io.FileMonitorFactory;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.NioFileMonitor;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import tailminuseff.config.Configuration;
import tailminuseff.config.ConfigurationFactory;
import tailminuseff.swing.MainFrame;

public class Guice3Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(MainFrame.class);
        install(new FactoryModuleBuilder().implement(FileMonitor.class, NioFileMonitor.class).build(FileMonitorFactory.class));
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
