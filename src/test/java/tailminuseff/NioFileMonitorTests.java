package tailminuseff;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeoutException;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Verifications;

import org.junit.Ignore;
import org.junit.Test;

public class NioFileMonitorTests extends FileMonitorTests<NioFileMonitor> {

	@Override
	protected NioFileMonitor createTarget(File file) {
		return new NioFileMonitor(file);
	}

	@Test
	@Ignore
	public void readEntireFileEmitsNoEventsIfNoLineTerminator() throws Exception {
		Files.write(file.toPath(), "NoEnd".getBytes(), StandardOpenOption.APPEND);
		target.readEntireFile();
		new FullVerifications(mockListener) {};
	}
	
	@Test
	@Ignore
	public void readEntireFileEmitsLineWithoutEndingsOnEvent() throws Exception
	{
		Files.write(file.toPath(), "FirstLine\n".getBytes(), StandardOpenOption.APPEND);
		target.readEntireFile();
		new Verifications() {
			{
				LineAddedEvent evt;
				mockListener.lineRead(evt = withCapture());
				assertEquals("FirstLine",evt.getLine());
			}
		};
	}
}
