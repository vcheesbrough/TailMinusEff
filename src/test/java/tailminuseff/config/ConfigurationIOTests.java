package tailminuseff.config;

import java.awt.Rectangle;
import java.io.*;
import java.util.*;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;

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
        assertEquals(bounds, writeRead(config).getMainWindowBounds());
    }

    @Test
    public void openDialogDirectoryCanBeWrittenAndRead() throws IOException, ClassNotFoundException {
        final Configuration config = new Configuration();
        final File dir = new File("./openFileDialog");
        config.setOpenDialogDirectory(dir);
        assertEquals(dir, writeRead(config).getOpenDialogDirectory());
    }

    @Test
    public void openFielsCanBeWrittenAndRead() throws IOException, ClassNotFoundException {
        final List<File> input = Arrays.asList(new File[]{new File("./file1"), new File("./file2")});
        final Configuration config = new Configuration();
        config.setOpenFiles(input);
        assertEquals(input, writeRead(config).getOpenFiles());
    }

    private Configuration writeRead(Configuration input) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        target.write(baos, input);
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final Configuration read = new Configuration();
        target.readInto(bais, read);
        return read;
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

    @Test
    public void defaultConstructorUsesCorrectFile(@Mocked File file) {
        assertEquals(ConfigurationIO.DEFAULT_FILE, new ConfigurationIO().getFile());
    }
}
