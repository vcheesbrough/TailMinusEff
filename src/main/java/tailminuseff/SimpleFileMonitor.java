package tailminuseff;

import java.io.*;
import java.nio.channels.*;
import java.util.regex.*;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

public class SimpleFileMonitor extends FileMonitor {

	private static final Pattern linePattern = Pattern.compile(".*?\r?\n", Pattern.MULTILINE | Pattern.DOTALL);

	@Inject
	public SimpleFileMonitor(@Assisted File file) {
		super(file);
	}

	@Override
	public Void call() throws IOException {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				try (FileInputStream inputStream = new FileInputStream(getFile())) {
					try (Reader reader = new InputStreamReader(inputStream)) {
						final StringBuilder stringBuilder = new StringBuilder();
						while (!Thread.currentThread().isInterrupted()) {
							final char buffer[] = new char[8192];
							final int bytesRead = reader.read(buffer);
							if (bytesRead > 0) {
								stringBuilder.append(buffer, 0, bytesRead);
								parseInputAndFireAddedEvents(stringBuilder);
							} else if ((bytesRead == -1) || (bytesRead == 0)) {
								final FileChannel channel = inputStream.getChannel();
								if (!getFile().exists() || (channel.position() > getFile().length())) {
									invokeListenersWithReset();
									break;
								} else {
									Thread.sleep(100);
								}
							}
						}
					}
				} catch (final FileNotFoundException fnfex) {
					// expected
					invokeListenersWithReset();
					Thread.sleep(100);
				}
			}
		} catch (final InterruptedException e) {
			// expected
		} catch (final ClosedByInterruptException e) {
			// I guess we expect this too
		}
		return null;
	}

	private void parseInputAndFireAddedEvents(final StringBuilder stringBuilder) {
		final Matcher matcher = linePattern.matcher(stringBuilder);
		while (matcher.find()) {
			final String line = stringBuilder.substring(matcher.start(), matcher.end());
			invokeListenersWithAdded(line);
			stringBuilder.delete(0, matcher.end());
			matcher.reset(stringBuilder);
		}
	}
}
