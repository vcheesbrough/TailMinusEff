package tailminuseff;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import javafx.stage.Stage;
import tailminuseff.config.Configuration;
import tailminuseff.config.ConfigurationFactory;
import tailminuseff.fx.FileViewController;
import tailminuseff.fx.FileViewControllerFactory;
import tailminuseff.io.FileMonitor;
import tailminuseff.io.FileMonitorFactory;
import tailminuseff.io.NioFileMonitor;

public class Guice3Module extends AbstractModule {

    public static Injector CreateInjector(Stage stage) {
        final Injector injector = Guice.createInjector(new Guice3Module(stage));
        return injector;
    }

    private final Stage stage;

    private Guice3Module(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(FileMonitor.class, NioFileMonitor.class).build(FileMonitorFactory.class));
        install(new FactoryModuleBuilder().implement(FileViewController.class, FileViewController.class).build(FileViewControllerFactory.class));
    }

    @Provides
    @Singleton
    EventBus providesEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    Stage providesStage() {
        return stage;
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
