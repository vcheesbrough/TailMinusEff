package tailminuseff.swing;

import tailminuseff.swing.FileTabComponent;
import tailminuseff.swing.FileContentDisplayPanel;
import mockit.*;
import org.junit.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import tailminuseff.*;

@Ignore
public class FileContentDisplayPanelTests {

    @Mocked
    private FileLineModel mockModel;

    @Before
    public void Setup() {
        // new MockUp<SwingUtilities>() {
        // @Mock
        // public void invokeLater(Runnable doRun) {
        // doRun.run();
        // }
        // };
    }

    @Test
    public void getFileLineModelReturnsSetInstance() {
        final FileContentDisplayPanel target = new FileContentDisplayPanel();
        target.setFileLineModel(mockModel);
        assertSame(mockModel, target.getFileLineModel());
    }

    @Test
    public void setFileLineModelAddsListener(@Mocked FileTabComponent tabComponent) {
        final FileContentDisplayPanel target = new FileContentDisplayPanel();
        target.setFileTabComponent(tabComponent);
        target.setFileLineModel(mockModel);

        new Verifications() {
            {
                mockModel.addListener((FileLineModelListener) any);
            }
        };
    }

    @Test
    public void setFileLineModelThrowsExceptionIfCalledTwice(@Mocked FileLineModel mockModel) {
        final FileContentDisplayPanel target = new FileContentDisplayPanel();
        target.setFileLineModel(mockModel);
        try {
            target.setFileLineModel(mockModel);
            fail("Exception expected");
        } catch (final IllegalStateException isex) {
            // expected
        }
    }
}
