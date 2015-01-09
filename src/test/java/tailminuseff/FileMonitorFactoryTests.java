package tailminuseff;

import java.io.File;

import mockit.*;

import org.junit.Test;

public class FileMonitorFactoryTests {

	@Mocked
	private SimpleFileMonitor mockMonitor;

	@Test
	public void monitorTypeConstructorCalled(@Mocked File file) {
		FileMonitorFactory.createForFile(file);
		new Verifications() {
			{
				new SimpleFileMonitor(file);
			}
		};
	}
}
