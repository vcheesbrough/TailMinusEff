package tailminuseff.ui;

import java.awt.*;
import java.awt.event.*;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tailminuseff.Guice3Module;
import tailminuseff.config.Configuration;
import tailminuseff.ui.actions.*;

import com.google.inject.*;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -1197142382069507042L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				// final MainFrame frame = new MainFrame();
				Guice.createInjector(new Guice3Module()).getInstance(MainFrame.class).setVisible(true);
				// frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private final ComponentListener boundsListener = new ComponentAdapter() {

		@Override
		public void componentMoved(ComponentEvent e) {
			configuration.setMainWindowBounds(getBounds());
		}

		@Override
		public void componentResized(ComponentEvent e) {
			configuration.setMainWindowBounds(getBounds());
		}

	};
	private final Configuration configuration;

	private final JPanel contentPane;

	private final MultiFileModelSwingAdaptorListener modelListener = new MultiFileModelSwingAdaptorListener() {

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

		@Override
		public void fileOpened(FileOpenedEvent evt) {
			final FileContentDisplayPanel panel = panelProvider.get();
			panel.setFileLineModel(evt.getFileLineModel());
			tabbedPane.add(panel);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), panel.getFileTabComponent());
		}
	};
	private final JTabbedPane tabbedPane;
	private final Provider<FileContentDisplayPanel> panelProvider;

	@SuppressWarnings("unused")
	@Inject
	public MainFrame(MultiFileModelSwingAdaptor multiFileModel, Configuration config, OpenFileAction openFileAction, ExitAction exitAction, Provider<FileContentDisplayPanel> panelProvider) {
		this.configuration = config;
		this.panelProvider = panelProvider;
		// openAction = openFileAction;
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

		final JMenuItem menuItemOpen = fileMenu.add(openFileAction);
		final JMenuItem menuItemExit = fileMenu.add(exitAction);

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
