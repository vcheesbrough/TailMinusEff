package tailminuseff.ui;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import mockit.*;

import org.junit.*;

import com.google.inject.*;

import tailminuseff.*;

public class FileContentDisplayPanelTests {

	@Mocked
	private FileLineModel mockModel;

	@Test
	public void getFileLineModelReturnsSetInstance() {
		final FileContentDisplayPanel target = new FileContentDisplayPanel();
		target.setFileLineModel(mockModel);
		assertSame(mockModel, target.getFileLineModel());
	}

	@Test
	@Ignore
	public void setFileLineModelAddsListener() {
		new FileContentDisplayPanel().setFileLineModel(mockModel);

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
