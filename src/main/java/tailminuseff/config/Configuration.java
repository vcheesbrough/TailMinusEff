package tailminuseff.config;

import java.awt.Rectangle;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.inject.Singleton;

@Singleton
public class Configuration implements Serializable {

    public static final String SELECTED_FILE = "selectedFile";

    private static final String OPEN_FILES = "openFiles";

    private static final String OPEN_DIALOG_DIRECTORY = "openDialogDirectory";

    public static final String MAIN_WINDOW_BOUNDS = "mainWindowBounds";

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

    private List<File> openFiles = new ArrayList<File>();

    public List<File> getOpenFiles() {
        return Collections.unmodifiableList(openFiles);
    }

    public void setOpenFiles(List<File> openFiles) {
        final List<File> oldValue = this.getOpenFiles();
        this.openFiles = new ArrayList<File>(openFiles);
        propertyChangeSupport.firePropertyChange(OPEN_FILES, oldValue, this.openFiles);
    }

    private File selectedFile;

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        File oldSelectedFile = this.selectedFile;
        this.selectedFile = selectedFile;
        propertyChangeSupport.firePropertyChange(SELECTED_FILE, oldSelectedFile, selectedFile);
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

    @Override
    public String toString() {
        return "Configuration{" + "selectedFile=" + selectedFile + ", mainWindowBounds=" + mainWindowBounds + ", openDialogDirectory=" + openDialogDirectory + ", openFiles=" + openFiles + '}';
    }

}
