package tailminuseff.ui;

import java.awt.*;
import java.awt.event.*;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.inject.*;

import tailminuseff.Guice3Module;
import tailminuseff.config.*;
import tailminuseff.ui.actions.*;

public class MainFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				// final MainFrame frame = new MainFrame();
				Guice3Module.getInjector().getInstance(MainFrame.class).setVisible(true);
				// frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static final long serialVersionUID = -1197142382069507042L;
	private final JPanel contentPane;
	private final Action exitAction = new ExitAction();
	private final JTabbedPane tabbedPane;

	private final Action openAction = Guice3Module.getInjector().getInstance(OpenFileAction.class);

	private final MultiFileModelSwingAdaptorListener modelListener = new MultiFileModelSwingAdaptorListener() {

		@Override
		public void fileOpened(FileOpenedEvent evt) {
			final FileContentDisplayPanel panel = new FileContentDisplayPanel();
			panel.setFileLineModel(evt.getFileLineModel());
			tabbedPane.add(panel);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), panel.createTabComponent());
		}

		@Override
		public void fileClosed(FileClosedEvent evt) {
			for (final Component component : tabbedPane.getComponents()) {
				if (component instanceof FileContentDisplayPanel) {
					final FileContentDisplayPanel panel = (FileContentDisplayPanel) component;
					if (evt.getFileLineModel().equals(panel.getFileLineModel())) {
						tabbedPane.remove(component);
						break;
					}
				}
			}
		}
	};

	private final ComponentListener boundsListener = new ComponentAdapter() {

		@Override
		public void componentResized(ComponentEvent e) {
			configuration.setMainWindowBounds(getBounds());
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			configuration.setMainWindowBounds(getBounds());
		}

	};
	private final Configuration configuration;

	@SuppressWarnings("unused")
	@Inject
	public MainFrame(MultiFileModelSwingAdaptor multiFileModel, Configuration config) {
		this.configuration = config;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		menuBar.add(fileMenu);

		final JMenuItem mntmExit = fileMenu.add(exitAction);

		final JMenuItem menuItem = fileMenu.add(openAction);
		multiFileModel.addListener(modelListener);

		addComponentListener(boundsListener);
		initializeBounds(config);
	}

	private void initializeBounds(Configuration config) {
		final Rectangle configBounds = config.getMainWindowBounds();
		if (configBounds != null) {
			setBounds(configBounds);
		}
	}
}
