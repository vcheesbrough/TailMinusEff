package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import mockit.*;

import org.junit.Test;

import tailminuseff.config.Configuration;
import tailminuseff.ui.MultiFileModelSwingAdaptor;

public class OpenFileActionTests {
	@Mocked
	private MultiFileModelSwingAdaptor mockModel;
	@Mocked
	private JFileChooser mockFileChoooser;
	@Mocked
	private Configuration mockConfiguration;

	@Test
	public void callsModelOpenIfDialogAccept(@Mocked Component eventSource) {
		new Expectations() {
			{
				mockConfiguration.getOpenDialogDirectory();
				result = System.getProperty("user.home");
			}
		};
		new OpenFileAction(mockModel, mockConfiguration).actionPerformed(new ActionEvent(eventSource, 0, "./someFile"));
		new Verifications() {
			{
				mockModel.openFile((File) any);
				times = 1;
			}
		};
	}
}
