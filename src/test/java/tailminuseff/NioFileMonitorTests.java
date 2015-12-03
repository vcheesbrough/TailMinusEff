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
}
