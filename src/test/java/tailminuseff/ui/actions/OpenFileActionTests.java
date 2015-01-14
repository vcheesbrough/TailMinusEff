package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import mockit.*;

import org.junit.Test;

import tailminuseff.config.ConfigurationFactory;
import tailminuseff.ui.MultiFileModelSwingAdaptor;

public class OpenFileActionTests {
	@Mocked
	private MultiFileModelSwingAdaptor mockModel;
	@Mocked
	private JFileChooser mockFileChoooser;
	@Mocked
	private ConfigurationFactory mockConfigurationFactory;

	@Test
	public void callsModelOpenIfDialogAccept(@Mocked Component eventSource) {
		new Expectations() {
			{
				mockConfigurationFactory.getConfiguration().getOpenDialogDirectory();
				result = System.getProperty("user.home");
			}
		};
		new OpenFileAction().actionPerformed(new ActionEvent(eventSource, 0, "./someFile"));
		new Verifications() {
			{
				MultiFileModelSwingAdaptor.getInstance().openFile((File) any);
				times = 1;
			}
		};
	}
}
