package tailminuseff.ui;

import static org.junit.Assert.*;
import mockit.Mocked;

import org.junit.*;

import tailminuseff.FileLineModel;
import tailminuseff.ui.actions.CloseFileAction;

public class FileTabComponentTests {

	@Test
	public void testGetAndSetModel(@Mocked FileLineModel mockModel, @Mocked CloseFileAction closeFileAction) {
		FileTabComponent target = new FileTabComponent(closeFileAction);
		target.setModel(mockModel);
		assertEquals(mockModel, target.getModel());
	}
}
