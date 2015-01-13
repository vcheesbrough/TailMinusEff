package tailminuseff.config;

import java.beans.*;
import java.io.*;

public class ConfigurationIO {

	public static final String DEFAULT_FILE = ".tailminuseff.config";

	public static void writeToDefaultFile(Configuration c) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.home"), DEFAULT_FILE))) {
			write(fos, c);
		}
	}

	public static void write(OutputStream out, Configuration config) {
		try (XMLEncoder encoder = new XMLEncoder(out)) {
			encoder.writeObject(config);
		}
	}

	public static void readIntoFromDefaultFile(Configuration c) throws FileNotFoundException, IOException {
		try (FileInputStream fin = new FileInputStream(new File(System.getProperty("user.home"), DEFAULT_FILE))) {
			readInto(fin, c);
		}
	}

	public static void readInto(InputStream in, Configuration destination) {
		try (XMLDecoder decoder = new XMLDecoder(in)) {
			final Configuration read = (Configuration) decoder.readObject();
			destination.setMainWindowBounds(read.getMainWindowBounds());
		}
	}
}
