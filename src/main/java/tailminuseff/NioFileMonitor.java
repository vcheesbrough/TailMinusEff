package tailminuseff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

public class NioFileMonitor extends FileMonitor {

	private static final Pattern linePattern = Pattern.compile("(?<line>.*?)\r?\n");
	
	@Inject
	public NioFileMonitor(@Assisted File file) {
		super(file);
	}

	@Override
	public Void call() throws IOException {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				try
				{
					readEntireFile();
				} catch (final NoSuchFileException|AccessDeniedException exception) {
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

	public void readEntireFile() throws IOException, InterruptedException {
		try (FileChannel channel = FileChannel.open(getFile().toPath(),StandardOpenOption.READ)) {
			final StringBuilder stringBuilder = new StringBuilder();
			Charset cs = Charset.forName("ASCII");
			ByteBuffer buffer = ByteBuffer.allocate(8192);
			while(!Thread.currentThread().isInterrupted())
			{
				int bytesRead = channel.read(buffer);
				if(bytesRead > 0)
				{
					buffer.limit(bytesRead);
					buffer.rewind();
					String line = cs.decode(buffer).toString();
					stringBuilder.append(line);
					parseInputAndFireAddedEvents(stringBuilder);
				}
				else
				{
					if(!getFile().exists() || Files.size(getFile().toPath()) < channel.position())
					{
						break;
					}
					Thread.sleep(50);
				}
				buffer.clear();
			}
		}
	}

	private void parseInputAndFireAddedEvents(final StringBuilder stringBuilder) {
		final Matcher matcher = linePattern.matcher(stringBuilder.toString());
		while (matcher.find()) {
			final String line = matcher.group("line");
			invokeListenersWithAdded(line);
			stringBuilder.delete(0, matcher.end());
		}
	}
}
