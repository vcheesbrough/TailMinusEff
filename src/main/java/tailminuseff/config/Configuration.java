package tailminuseff.config;

import java.awt.Rectangle;
import java.beans.*;
import java.io.*;

public class Configuration implements Serializable {

	private static final String OPEN_DIALOG_DIRECTORY = "openDialogDirectory";

	public static final String MAIN_WINDOW_BOUNDS = "mainWindowBounds";

	private static final long serialVersionUID = 7438568253935637557L;

	private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	private Rectangle mainWindowBounds;

	public Rectangle getMainWindowBounds() {
		return mainWindowBounds;
	}

	public void setMainWindowBounds(Rectangle mainWindowBounds) {
		final Rectangle oldValue = this.mainWindowBounds;
		this.mainWindowBounds = mainWindowBounds;
		propertyChangeSupport.firePropertyChange(MAIN_WINDOW_BOUNDS, oldValue, this.mainWindowBounds);
	}

	private File openDialogDirectory = new File(System.getProperty("user.home"));

	public File getOpenDialogDirectory() {
		return openDialogDirectory;
	}

	public void setOpenDialogDirectory(File openDialogDirectory) {
		final File oldValue = this.openDialogDirectory;
		this.openDialogDirectory = openDialogDirectory;
		propertyChangeSupport.firePropertyChange(OPEN_DIALOG_DIRECTORY, oldValue, this.openDialogDirectory);
	}

	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(property, listener);
	}

	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(property, listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
}