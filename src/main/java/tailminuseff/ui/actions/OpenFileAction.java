package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;

import tailminuseff.ui.MultiFileModelSwingAdaptor;

public class OpenFileAction extends AbstractAction {
	private static final long serialVersionUID = -3269668944814053215L;

	public OpenFileAction() {
		putValue(NAME, "Open");
		putValue(SHORT_DESCRIPTION, "Open a file");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		final int result = fileChooser.showOpenDialog((Component) e.getSource());
		if (result == JFileChooser.APPROVE_OPTION) {
			MultiFileModelSwingAdaptor.getInstance().openFile(fileChooser.getSelectedFile());
		}
	}
}