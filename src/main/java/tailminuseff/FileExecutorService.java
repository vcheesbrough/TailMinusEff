package tailminuseff;

import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class FileExecutorService {

	private static ExecutorService executorService;

	public synchronized static ExecutorService getExecutorService() {
		if (executorService == null) {
			final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
			threadFactoryBuilder.setNameFormat("FileExecutorService-Pool-%d");
			threadFactoryBuilder.setDaemon(true);
			threadFactoryBuilder.setPriority(Thread.MIN_PRIORITY);
			executorService = Executors.newCachedThreadPool(threadFactoryBuilder.build());
		}
		return executorService;
	}
}
