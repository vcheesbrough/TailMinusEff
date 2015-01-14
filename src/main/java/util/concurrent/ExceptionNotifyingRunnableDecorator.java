package util.concurrent;

public class ExceptionNotifyingRunnableDecorator implements Runnable {

	private final Runnable delegate;
	private final UnhandledExceptionConsumer callback;

	public ExceptionNotifyingRunnableDecorator(Runnable delegate, UnhandledExceptionConsumer callback) {
		super();
		this.delegate = delegate;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			delegate.run();
		} catch (final RuntimeException rex) {
			this.callback.acceptException(Thread.currentThread(), rex);
			throw rex;
		} catch (final Error err) {
			this.callback.acceptException(Thread.currentThread(), err);
			throw err;
		}
	}
}
