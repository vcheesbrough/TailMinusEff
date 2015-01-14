package tailminuseff.config;

import java.io.*;

public class ConfigurationIO {

	public static final String DEFAULT_FILE = ".tailminuseff.config";

	public static void writeToDefaultFile(Configuration c) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.home"), DEFAULT_FILE))) {
			write(fos, c);
		}
	}

	public static void write(OutputStream out, Configuration config) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(out)) {
			encoder.writeObject(config);
		}
	}

	public static void readIntoFromDefaultFile(Configuration c) throws FileNotFoundException, IOException, ClassNotFoundException {
		try (FileInputStream fin = new FileInputStream(new File(System.getProperty("user.home"), DEFAULT_FILE))) {
			readInto(fin, c);
		}
	}

	public static void readInto(InputStream in, Configuration destination) throws IOException, ClassNotFoundException {
		try (ObjectInputStream decoder = new ObjectInputStream(in)) {
			final Configuration read = (Configuration) decoder.readObject();
			destination.setMainWindowBounds(read.getMainWindowBounds());
			destination.setOpenDialogDirectory(read.getOpenDialogDirectory());
		}
	}
}
