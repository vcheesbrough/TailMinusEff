package tailminuseff.config;

import java.awt.Rectangle;
import java.beans.*;
import java.io.Serializable;

public class Configuration implements Serializable {

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