package tailminuseff.ui;

import java.awt.*;

import javax.inject.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tailminuseff.FileLineModel;
import tailminuseff.ui.actions.CloseFileAction;

public class FileContentDisplayPanel extends JPanel {
	private static final long serialVersionUID = -1364890050702175341L;
	private final JTextPane textPane;

	private FileLineModel fileLineModel;

	@Inject
	public FileContentDisplayPanel() {
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));

		final JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		textPane = new JTextPane();
		textPane.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 13));
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
	}

	public FileLineModel getFileLineModel() {
		return fileLineModel;
	}

	public void setFileLineModel(FileLineModel fileLineModel) {
		if (this.fileLineModel != null) {
			throw new IllegalStateException();
		}
		this.fileLineModel = fileLineModel;
		new FileLineModelDocumentAdaptor(textPane.getDocument(), this.fileLineModel);
		setName(this.fileLineModel.getFile().getName());
		if (fileTabComponent != null) {
			fileTabComponent.setModel(fileLineModel);
		}
	}

	private FileTabComponent fileTabComponent;

	public FileTabComponent getFileTabComponent() {
		return fileTabComponent;
	}

	@Inject
	public void setFileTabComponent(FileTabComponent tabComponent) {
		this.fileTabComponent = tabComponent;
		fileTabComponent.setModel(getFileLineModel());
	}
}
