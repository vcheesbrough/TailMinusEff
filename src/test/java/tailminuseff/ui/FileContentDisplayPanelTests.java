package tailminuseff.ui;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.*;

import org.junit.*;

import tailminuseff.*;

public class FileContentDisplayPanelTests {

	@Test
	public void getFileLineModelReturnsSetInstance(@Mocked FileLineModel mockModel) {
		final FileContentDisplayPanel target = new FileContentDisplayPanel();
		target.setFileLineModel(mockModel);
		assertSame(mockModel, target.getFileLineModel());
	}

	@Test
	@Ignore
	public void setFileLineModelAddsListener(@Mocked FileLineModel mockModel) {
		new FileContentDisplayPanel().setFileLineModel(mockModel);

		new Verifications() {
			{
				mockModel.addListener((FileLineModelListener) any);
			}
		};
	}

	@Test
	public void createFileTabComponentReturnsFileTabComponentWithCorrectModel(@Mocked FileLineModel mockModel) {
		final FileContentDisplayPanel target = new FileContentDisplayPanel();
		target.setFileLineModel(mockModel);
		assertTrue(target.createTabComponent() instanceof FileTabComponent);
		assertSame(mockModel, ((FileTabComponent) target.createTabComponent()).getModel());
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
