package tailminuseff;

import java.util.concurrent.*;

import javax.inject.Singleton;

import util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Singleton
public class ApplicationExecutors {

	public ExecutorService createFilesExecutorService() {
		final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
		threadFactoryBuilder.setNameFormat("FileExecutorService-Pool-%d");
		threadFactoryBuilder.setDaemon(true);
		threadFactoryBuilder.setPriority(Thread.MIN_PRIORITY);
		return new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);

	}

	public ExecutorService createGeneralExecutorService() {
		final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
		threadFactoryBuilder.setNameFormat("GeneralExecutorService-Pool-%d");
		return new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);
	}

	public ScheduledExecutorService createScheduledExecutorService() {
		final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
		threadFactoryBuilder.setNameFormat("ScheduledExecutorService-Pool-%d");
		return new ExceptionNotifyingScheduledExecutorServiceDecorator(Executors.newScheduledThreadPool(1, threadFactoryBuilder.build()), unhandledExceptionConsumer);
	}

	private UnhandledExceptionConsumer unhandledExceptionConsumer = (thread, throwable) -> {
		System.out.println(thread.getName());
		throwable.printStackTrace();
	};
}
