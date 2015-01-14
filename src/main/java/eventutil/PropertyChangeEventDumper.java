package eventutil;

import java.beans.*;

public class PropertyChangeEventDumper implements PropertyChangeListener {

	private final String message;

	public PropertyChangeEventDumper(String message) {
		super();
		this.message = message;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(message + " " + evt);
	}
}
