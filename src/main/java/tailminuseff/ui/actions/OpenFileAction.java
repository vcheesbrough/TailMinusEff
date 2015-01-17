package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.inject.*;
import javax.swing.*;

import tailminuseff.config.*;
import tailminuseff.ui.MultiFileModelSwingAdaptor;

@Singleton
public class OpenFileAction extends AbstractAction {
	private static final long serialVersionUID = -3269668944814053215L;

	private final MultiFileModelSwingAdaptor model;
	private final Configuration config;

	@Inject
	public OpenFileAction(MultiFileModelSwingAdaptor model, Configuration config) {
		this.model = model;
		this.config = config;
		putValue(NAME, "Open");
		putValue(SHORT_DESCRIPTION, "Open a file");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(config.getOpenDialogDirectory());
		final int result = fileChooser.showOpenDialog((Component) e.getSource());
		config.setOpenDialogDirectory(fileChooser.getCurrentDirectory());
		if (result == JFileChooser.APPROVE_OPTION) {
			model.openFile(fileChooser.getSelectedFile());
		}
	}
}