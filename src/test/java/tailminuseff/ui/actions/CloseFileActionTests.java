package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;
import mockit.*;
import org.junit.Test;
import tailminuseff.ui.MultiFileModelSwingAdaptor;

public class CloseFileActionTests {

    @Mocked
    private MultiFileModelSwingAdaptor mockModel;

    @Mocked
    private ImageIcon mockImageIcon;

    @Test
    public void callsMockModelCloseWithFile(@Mocked Component eventSource) {
        new CloseFileAction(mockModel).actionPerformed(new ActionEvent(eventSource, 0, "fileName"));

        new Verifications() {
            {
                mockModel.closeFile(new File("fileName"));
                times = 1;
            }
        };
    }
}
