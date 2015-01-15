package tailminuseff.config;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.io.*;

import mockit.*;

import org.junit.*;

public class ConfigurationIOTests {

	private ConfigurationIO target;
	private File targetFile;

	@Before
	public void Setup() throws IOException {
		targetFile = File.createTempFile("ConfigurationIOTests", "config");
		targetFile.deleteOnExit();
		target = new ConfigurationIO(targetFile);
	}

	@Test
	public void mainWindowBoundsCanBeWrittenAndRead() throws ClassNotFoundException, IOException {
		final Configuration config = new Configuration();
		final Rectangle bounds = new Rectangle(10, 20, 50, 100);
		config.setMainWindowBounds(bounds);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		target.write(baos, config);
		target.write(System.out, config);
		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Configuration read = new Configuration();
		target.readInto(bais, read);
		assertEquals(bounds, read.getMainWindowBounds());
	}

	@Test
	public void openDialogDirectoryCanBeWrittenAndRead() throws IOException, ClassNotFoundException {
		final Configuration config = new Configuration();
		final File dir = new File("./openFileDialog");
		config.setOpenDialogDirectory(dir);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		target.write(baos, config);
		target.write(System.out, config);
		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Configuration read = new Configuration();
		target.readInto(bais, read);
		assertEquals(dir, read.getOpenDialogDirectory());
	}

	@Test
	public void writeToDefaultFileUsesFileOutputStreamWithCorrectFile() throws FileNotFoundException, IOException {
		final Configuration config = new Configuration();
		target.writeToDefaultFile(config);

		new Verifications() {
			{
				new FileOutputStream(targetFile);
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
		target.readIntoFromDefaultFile(config);
		new Verifications() {
			{
				new FileInputStream(targetFile);
			}
		};
	}
}
