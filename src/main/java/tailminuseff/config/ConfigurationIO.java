package tailminuseff.config;

import com.sun.istack.internal.logging.Logger;
import java.io.*;

public class ConfigurationIO {

    public static final File DEFAULT_FILE = new File(System.getProperty("user.home"), ".tailminuseff.config");

    private final File file;

    public File getFile() {
        return file;
    }

    public ConfigurationIO() {
        this(DEFAULT_FILE);
    }

    public ConfigurationIO(File file) {
        super();
        this.file = file;
    }

    public void writeToDefaultFile(Configuration c) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(this.file)) {
            write(fos, c);
        }
        Logger.getLogger(this.getClass()).config("Wrote Config to file \"{0}\": {1}", new Object[]{file, c});
    }

    public void write(OutputStream out, Configuration config) throws IOException {
        try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
            encoder.writeObject(config);
        }
    }

    public void readIntoFromDefaultFile(Configuration c) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (FileInputStream fin = new FileInputStream(this.file)) {
            readInto(fin, c);
        }
        Logger.getLogger(this.getClass()).config("Read Config from file \"{0}\": {1}", new Object[]{file, c});
    }

    public void readInto(InputStream in, Configuration destination) throws IOException, ClassNotFoundException {
        try (ObjectInputStream decoder = new ObjectInputStream(in)) {
            final Configuration read = (Configuration) decoder.readObject();
            destination.setMainWindowBounds(read.getMainWindowBounds());
            destination.setOpenDialogDirectory(read.getOpenDialogDirectory());
            destination.setOpenFiles(read.getOpenFiles());
            destination.setSelectedFile(read.getSelectedFile());
        }
    }
}
