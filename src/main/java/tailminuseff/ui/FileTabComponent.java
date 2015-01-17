package tailminuseff.ui;

import java.awt.*;

import javax.swing.*;

import tailminuseff.*;
import tailminuseff.ui.actions.CloseFileAction;

public class FileTabComponent extends JPanel {
	private final JLabel fileNameLbl;
	private final JButton closeButton;

	public FileTabComponent() {
		setOpaque(false);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 66, 32, 0 };
		gridBagLayout.rowHeights = new int[] { 28, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		fileNameLbl = new JLabel("FILE NAME");
		final GridBagConstraints gbc_fileNameLbl = new GridBagConstraints();
		gbc_fileNameLbl.weighty = 1.0;
		gbc_fileNameLbl.weightx = 1.0;
		gbc_fileNameLbl.anchor = GridBagConstraints.EAST;
		gbc_fileNameLbl.insets = new Insets(0, 0, 0, 5);
		gbc_fileNameLbl.gridx = 0;
		gbc_fileNameLbl.gridy = 0;
		add(fileNameLbl, gbc_fileNameLbl);

		closeButton = new JButton("");
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setHideActionText(true);
		closeButton.setAction(closeFileAction);
		closeButton.setDefaultCapable(false);
		closeButton.setFocusable(false);
		// closeButton.putClientProperty("JButton.buttonType", "recessed");
		final GridBagConstraints gbc_closeButton = new GridBagConstraints();
		gbc_closeButton.weighty = 1.0;
		gbc_closeButton.weightx = 1.0;
		gbc_closeButton.anchor = GridBagConstraints.WEST;
		gbc_closeButton.gridx = 1;
		gbc_closeButton.gridy = 0;
		add(closeButton, gbc_closeButton);
	}

	public FileLineModel getModel() {
		return model;
	}

	public void setModel(FileLineModel model) {
		this.model = model;
		this.fileNameLbl.setText(model.getFile().getName());
		this.closeButton.setActionCommand(model.getFile().getAbsolutePath());
	}

	private FileLineModel model;
	private final CloseFileAction closeFileAction = Guice3Module.getInjector().getInstance(CloseFileAction.class);
}
