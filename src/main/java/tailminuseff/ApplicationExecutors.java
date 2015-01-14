package tailminuseff;

import java.util.concurrent.*;

import util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ApplicationExecutors {

	public synchronized static ExecutorService getFilesExecutorService() {
		if (fileExecutorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("FileExecutorService-Pool-%d");
			threadFactoryBuilder.setDaemon(true);
			threadFactoryBuilder.setPriority(Thread.MIN_PRIORITY);
			fileExecutorService = new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);
		}
		return fileExecutorService;
	}

	public synchronized static ExecutorService getGeneralExecutorService() {
		if (generalExecutorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("GeneralExecutorService-Pool-%d");
			generalExecutorService = new ExceptionNotifyingExecutorServiceDecorator(Executors.newCachedThreadPool(threadFactoryBuilder.build()), unhandledExceptionConsumer);
		}
		return generalExecutorService;
	}

	public synchronized static ScheduledExecutorService getScheduledExecutorService() {
		if (scheduledExecutorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("ScheduledExecutorService-Pool-%d");
			scheduledExecutorService = new ExceptionNotifyingScheduledExecutorServiceDecorator(Executors.newScheduledThreadPool(1, threadFactoryBuilder.build()), unhandledExceptionConsumer);
		}
		return scheduledExecutorService;
	}

	private static UnhandledExceptionConsumer unhandledExceptionConsumer = (thread, throwable) -> {
		System.out.println(thread.getName());
		throwable.printStackTrace();
	};

	private static ExecutorService fileExecutorService;

	private static ExecutorService generalExecutorService;

	private static ScheduledExecutorService scheduledExecutorService;

}
