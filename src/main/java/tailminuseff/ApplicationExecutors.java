package tailminuseff;

import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ApplicationExecutors {

	public synchronized static ExecutorService getFilesExecutorService() {
		if (fileExecutorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("FileExecutorService-Pool-%d");
			threadFactoryBuilder.setDaemon(true);
			threadFactoryBuilder.setPriority(Thread.MIN_PRIORITY);
			fileExecutorService = Executors.newCachedThreadPool(threadFactoryBuilder.build());
		}
		return fileExecutorService;
	}

	public synchronized static ExecutorService getGeneralExecutorService() {
		if (generalExecutorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("GeneralExecutorService-Pool-%d");
			generalExecutorService = Executors.newCachedThreadPool(threadFactoryBuilder.build());
		}
		return generalExecutorService;
	}

	private static ExecutorService fileExecutorService;

	private static ExecutorService generalExecutorService;
}
