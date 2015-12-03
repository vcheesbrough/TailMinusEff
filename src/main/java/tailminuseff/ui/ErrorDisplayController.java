package tailminuseff.ui;

import javax.inject.*;
import javax.swing.*;

import tailminuseff.UnhandledException;

import com.google.common.eventbus.*;

public class ErrorDisplayController {
	private final Provider<MainFrame> mainFrameProvider;

	@Inject
	public ErrorDisplayController(EventBus eventBus, Provider<MainFrame> mainFrameProvider) {
		super();
		this.mainFrameProvider = mainFrameProvider;
		eventBus.register(this);
	}

	@Subscribe
	public void receiveException(UnhandledException error) {
		SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(mainFrameProvider.get(), error.getMessage()));
	}

}