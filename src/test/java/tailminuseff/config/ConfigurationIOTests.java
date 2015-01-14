package tailminuseff.config;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.io.*;

import mockit.*;

import org.junit.Test;

public class ConfigurationIOTests {

	@Test
	public void mainWindowBoundsCanBeWrittenAndRead() throws ClassNotFoundException, IOException {
		final Configuration config = new Configuration();
		final Rectangle bounds = new Rectangle(10, 20, 50, 100);
		config.setMainWindowBounds(bounds);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ConfigurationIO.write(baos, config);
		ConfigurationIO.write(System.out, config);
		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Configuration read = new Configuration();
		ConfigurationIO.readInto(bais, read);
		assertEquals(bounds, read.getMainWindowBounds());
	}

	@Test
	public void openDialogDirectoryCanBeWrittenAndRead() throws IOException, ClassNotFoundException {
		final Configuration config = new Configuration();
		final File dir = new File("./openFileDialog");
		config.setOpenDialogDirectory(dir);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ConfigurationIO.write(baos, config);
		ConfigurationIO.write(System.out, config);
		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Configuration read = new Configuration();
		ConfigurationIO.readInto(bais, read);
		assertEquals(dir, read.getOpenDialogDirectory());
	}

	@Test
	public void writeToDefaultFileUsesFileOutputStreamWithCorrectFile(@Injectable FileOutputStream fos) throws FileNotFoundException, IOException {
		final Configuration config = new Configuration();
		ConfigurationIO.writeToDefaultFile(config);

		new Verifications() {
			{
				new FileOutputStream(new File(System.getProperty("user.home"), ConfigurationIO.DEFAULT_FILE));
			}
		};
	}

	@Test
	public void readIntoFromDefaultFileReadsFromFileInputStreamWithCorrectFile(@Injectable FileInputStream fis) throws FileNotFoundException, IOException, ClassNotFoundException {
		new MockUp<ConfigurationIO>() {
			@Mock
			public void readInto(InputStream in, Configuration destination) {

			}
		};
		final Configuration config = new Configuration();
		ConfigurationIO.readIntoFromDefaultFile(config);
		new Verifications() {
			{
				new FileInputStream(new File(System.getProperty("user.home"), ConfigurationIO.DEFAULT_FILE));
			}
		};
	}
}
