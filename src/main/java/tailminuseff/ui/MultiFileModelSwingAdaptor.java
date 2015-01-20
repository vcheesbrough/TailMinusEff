package tailminuseff.ui;

import java.io.File;
import java.util.concurrent.*;

import javax.inject.*;
import javax.swing.SwingUtilities;

import tailminuseff.*;
import tailminuseff.config.Configuration;

import com.google.common.eventbus.EventBus;

import eventutil.*;

@Singleton
public class MultiFileModelSwingAdaptor implements EventProducer<MultiFileModelSwingAdaptorListener> {
	private final MultiFileModel delegate;

	private final EventListenerList<MultiFileModelSwingAdaptorListener> listeners = new EventListenerList<MultiFileModelSwingAdaptorListener>();

	private final ExecutorService executorService;

	private final EventBus eventBus;

	@Inject
	public MultiFileModelSwingAdaptor(Configuration config, MultiFileModel model, @GeneralExecutor ExecutorService executorService, EventBus eventBus) {
		delegate = model;
		this.executorService = executorService;
		this.eventBus = eventBus;
		new OpenFilesConfigHandler(config);
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
		executorService.submit(() -> {
			final FileLineModel newModel = delegate.openFile(newFile);
			final FileOpenedEvent evt = new FileOpenedEvent(this, newModel);
			SwingUtilities.invokeLater(() -> listeners.forEachLisener(listener -> listener.fileOpened(evt)));
		});
	}

	public void closeFile(File file) {
		executorService.submit(() -> {
			try {
				final FileLineModel model = delegate.close(file);
				final FileClosedEvent evt = new FileClosedEvent(this, model);
				SwingUtilities.invokeLater(() -> listeners.forEachLisener(listener -> listener.fileClosed(evt)));
			} catch (InterruptedException | ExecutionException ex) {
				eventBus.post(new UnhandledException(ex, "Unable to close file \"" + file + "\""));
			}
		});
	}

	private class OpenFilesConfigHandler {
		private final Configuration configuration;

		public OpenFilesConfigHandler(Configuration config) {
			this.configuration = config;
			this.configuration.getOpenFiles().forEach(f -> openFile(f));
			addListener(configWriter);
		}

		private final MultiFileModelSwingAdaptorListener configWriter = new MultiFileModelSwingAdaptorListener() {

			@Override
			public void fileOpened(FileOpenedEvent evt) {
				OpenFilesConfigHandler.this.configuration.setOpenFiles(delegate.getOpenFiles());
			}

			@Override
			public void fileClosed(FileClosedEvent evt) {
				OpenFilesConfigHandler.this.configuration.setOpenFiles(delegate.getOpenFiles());
			}
		};
	}
}
