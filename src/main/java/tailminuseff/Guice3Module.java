package tailminuseff;

import java.io.IOException;

import tailminuseff.config.*;
import tailminuseff.ui.MainFrame;
import tailminuseff.ui.actions.OpenFileAction;

import com.google.inject.*;

public class Guice3Module extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(MainFrame.class);
		bind(OpenFileAction.class);
		bind(Configuration.class).toProvider(ConfigurationFactory.class);
	}
}
