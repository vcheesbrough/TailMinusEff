package tailminuseff.ui;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import tailminuseff.*;
import eventutil.*;

public class MultiFileModelSwingAdaptor implements EventProducer<MultiFileModelSwingAdaptorListener> {
	private final MultiFileModel delegate = new MultiFileModel();

	private final EventListenerList<MultiFileModelSwingAdaptorListener> listeners = new EventListenerList<MultiFileModelSwingAdaptorListener>();

	private static MultiFileModelSwingAdaptor instance;

	public static final MultiFileModelSwingAdaptor getInstance() {
		if (instance == null) {
			instance = new MultiFileModelSwingAdaptor();
		}
		return instance;
	}

	MultiFileModelSwingAdaptor() {
	}

	@Override
	public void addListener(MultiFileModelSwingAdaptorListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public void removeListener(MultiFileModelSwingAdaptorListener listener) {
		listeners.removeListener(listener);
	}

	public void openFile(File newFile) {
		ApplicationExecutors.getGeneralExecutorService().submit(() -> {
			final FileLineModel newModel = delegate.openFile(newFile);
			final FileOpenedEvent evt = new FileOpenedEvent(this, newModel);
			SwingUtilities.invokeLater(() -> listeners.forEachLisener(listener -> listener.fileOpened(evt)));
		});
	}

	public void closeFile(File file) {
		ApplicationExecutors.getGeneralExecutorService().submit(() -> {
			try {
				final FileLineModel model = delegate.close(file);
				final FileClosedEvent evt = new FileClosedEvent(this, model);
				SwingUtilities.invokeLater(() -> listeners.forEachLisener(listener -> listener.fileClosed(evt)));
			} catch (final InterruptedException ex) {
				throw new RuntimeException(ex);
			} catch (final ExecutionException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
}