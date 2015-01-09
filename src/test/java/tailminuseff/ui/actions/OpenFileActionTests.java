package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import mockit.*;

import org.junit.*;

import tailminuseff.ui.MultiFileModelSwingAdaptor;

public class OpenFileActionTests {
	@Mocked
	private MultiFileModelSwingAdaptor mockModel;
	@Mocked
	private JFileChooser mockFileChoooser;

	@Test
	public void callsModelOpenIfDialogAccept(@Mocked Component eventSource) {
		new OpenFileAction().actionPerformed(new ActionEvent(eventSource, 0, ""));
		new Verifications() {
			{
				MultiFileModelSwingAdaptor.getInstance().openFile((File) any);
				times = 1;
			}
		};
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
