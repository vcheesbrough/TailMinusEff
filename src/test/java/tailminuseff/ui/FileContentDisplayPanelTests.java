package tailminuseff.ui;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import mockit.*;

import org.junit.*;

import tailminuseff.*;

public class FileContentDisplayPanelTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void setFileLineModelAddsListener(@Mocked FileLineModel mockModel) {
		new FileContentDisplayPanel().setFileLineModel(mockModel);

		new Verifications() {
			{
				mockModel.addListener((FileLineModelListener) any);
			}
		};
	}

	@Test
	public void getFileLineModelReturnsSetInstance(@Mocked FileLineModel mockModel) {
		final FileContentDisplayPanel target = new FileContentDisplayPanel();
		target.setFileLineModel(mockModel);
		assertSame(mockModel, target.getFileLineModel());
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
