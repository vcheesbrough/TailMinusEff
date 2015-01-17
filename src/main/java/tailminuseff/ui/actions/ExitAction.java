package tailminuseff.ui.actions;

import java.awt.event.ActionEvent;

import javax.inject.Singleton;
import javax.swing.AbstractAction;

@Singleton
public class ExitAction extends AbstractAction {
	private static final long serialVersionUID = -3203743002084752721L;

	public ExitAction() {
		putValue(NAME, "Exit");
		putValue(SHORT_DESCRIPTION, "Exit the Application");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}