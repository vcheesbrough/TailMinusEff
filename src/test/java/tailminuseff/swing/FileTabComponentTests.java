package tailminuseff.swing;

import tailminuseff.swing.FileTabComponent;
import mockit.Mocked;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import tailminuseff.FileLineModel;
import tailminuseff.swing.actions.CloseFileAction;

public class FileTabComponentTests {

    @Test
    public void testGetAndSetModel(@Mocked FileLineModel mockModel, @Mocked CloseFileAction closeFileAction) {
        final FileTabComponent target = new FileTabComponent(closeFileAction);
        target.setModel(mockModel);
        assertEquals(mockModel, target.getModel());
    }
}
