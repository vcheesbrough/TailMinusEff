package tailminuseff;

import java.io.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.*;

public class SimpleFileMonitor implements Callable<Void> {

	private static final Pattern linePattern = Pattern.compile(".*?\r?\n", Pattern.MULTILINE | Pattern.DOTALL);
	private final List<FileMonitorListener> listeners = new ArrayList<FileMonitorListener>();
	private final File file;

	public SimpleFileMonitor(File file) {
		this.file = file;
	}

	public void addListener(FileMonitorListener listener) {
		listeners.add(listener);
	}

	public void removeListener(FileMonitorListener listener) {
		listeners.remove(listener);
	}

	protected void invokeListenersWithAdded(String line) {
		final LineAddedEvent evt = new LineAddedEvent(this, line);
		for (final FileMonitorListener listener : listeners) {
			listener.lineRead(evt);
		}
	}

	protected void invokeListenersWithReset() {
		final FileResetEvent evt = new FileResetEvent(this);
		for (final FileMonitorListener listener : listeners) {
			listener.fileReset(evt);
		}
	}

	@Override
	public Void call() throws IOException, InterruptedException {
		final StringBuilder stringBuilder = new StringBuilder();

		try {
			while (!Thread.currentThread().isInterrupted()) {
				try (FileInputStream inputStream = new FileInputStream(file)) {
					try (Reader reader = new InputStreamReader(inputStream)) {
						while (!Thread.currentThread().isInterrupted()) {
							final char buffer[] = new char[8192];
							final int bytesRead = reader.read(buffer);
							if (bytesRead > 0) {
								stringBuilder.append(buffer, 0, bytesRead);
								final Matcher matcher = linePattern.matcher(stringBuilder);
								while (matcher.find()) {
									final String line = stringBuilder.substring(matcher.start(), matcher.end());
									invokeListenersWithAdded(line);
									stringBuilder.delete(0, matcher.end());
									matcher.reset(stringBuilder);
								}
							} else if ((bytesRead == -1) || (bytesRead == 0)) {
								final FileChannel channel = inputStream.getChannel();
								if (!file.exists() || (channel.position() > file.length())) {
									invokeListenersWithReset();
									break;
								} else {
									Thread.sleep(100);
								}
							}
						}
					}
				}
			}
		} catch (final InterruptedException e) {
			// expected
		} catch (final ClosedByInterruptException e) {
			// I guess we expect this too
		}
		return null;
	}
}
