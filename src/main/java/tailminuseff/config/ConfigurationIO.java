package tailminuseff.config;

import java.io.*;

public class ConfigurationIO {

	private final File file;

	public ConfigurationIO(File file) {
		super();
		this.file = file;
	}

	public void writeToDefaultFile(Configuration c) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(this.file)) {
			write(fos, c);
		}
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
	}

	public void readInto(InputStream in, Configuration destination) throws IOException, ClassNotFoundException {
		try (ObjectInputStream decoder = new ObjectInputStream(in)) {
			final Configuration read = (Configuration) decoder.readObject();
			destination.setMainWindowBounds(read.getMainWindowBounds());
			destination.setOpenDialogDirectory(read.getOpenDialogDirectory());
			destination.setOpenFiles(read.getOpenFiles());
		}
	}
}
