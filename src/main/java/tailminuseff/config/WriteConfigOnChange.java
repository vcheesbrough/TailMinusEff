package tailminuseff.config;

import java.beans.*;
import java.io.IOException;

public class WriteConfigOnChange implements PropertyChangeListener {

	private final ConfigurationIO configIO;
	private final Configuration config;

	public WriteConfigOnChange(Configuration config, ConfigurationIO configIO) {
		this.config = config;
		this.configIO = configIO;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			configIO.writeToDefaultFile(config);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
