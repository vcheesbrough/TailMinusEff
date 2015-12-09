package tailminuseff;

public class UnhandledException {

    private final Throwable thrown;
    private final Thread thread;
    private String message;

    public UnhandledException(Throwable thrown, String message) {
        this(thrown, Thread.currentThread(), message);
    }

    public UnhandledException(Throwable thrown, Thread thread, String message) {
        super();
        this.thrown = thrown;
        this.thread = thread;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrown() {
        return thrown;
    }

    public Thread getThread() {
        return thread;
    }
}
