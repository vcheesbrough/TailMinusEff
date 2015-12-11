package tailminuseff.swing.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.inject.*;
import javax.swing.*;
import tailminuseff.swing.MultiFileModelSwingAdaptor;

@Singleton
public class CloseFileAction extends AbstractAction {

    private static final long serialVersionUID = 3794223646705826118L;

    private final MultiFileModelSwingAdaptor model;

    @Inject
    public CloseFileAction(MultiFileModelSwingAdaptor model) {
        this.model = model;
        putValue(NAME, "Close");
        putValue(SHORT_DESCRIPTION, "Close");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/icons/Delete.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getActionCommand() != null) && !e.getActionCommand().equals("")) {
            model.closeFile(new File(e.getActionCommand()));
        }
    }
}
