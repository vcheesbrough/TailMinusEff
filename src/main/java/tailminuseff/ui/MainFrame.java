package tailminuseff.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tailminuseff.ui.actions.*;

public class MainFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				final MainFrame frame = new MainFrame();
				frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static final long serialVersionUID = -1197142382069507042L;
	private final JPanel contentPane;
	private final Action exitAction = new ExitAction();
	private final JTabbedPane tabbedPane;

	private final Action openAction = new OpenFileAction();

	private final MultiFileModelSwingAdaptorListener modelListener = evt -> {
		final FileContentDisplayPanel panel = new FileContentDisplayPanel();
		panel.setFileLineModel(evt.getFileLineModel());
		tabbedPane.add(panel);
	};

	/**
	 * Create the frame.
	 */
	public MainFrame() {
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
		MultiFileModelSwingAdaptor.getInstance().addListener(modelListener);
	}
}
