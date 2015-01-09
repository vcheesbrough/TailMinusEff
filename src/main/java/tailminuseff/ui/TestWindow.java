package tailminuseff.ui;

import java.awt.*;
import java.io.File;
import java.util.concurrent.Executors;

import javax.swing.*;

import tailminuseff.*;

public class TestWindow {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				final TestWindow window = new TestWindow();
				window.frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame frame;

	private final FileMonitor monitor;

	/**
	 * Create the application.
	 */
	public TestWindow() {
		monitor = new SimpleFileMonitor(new File("./test.txt"));

		initialize();

		Executors.newCachedThreadPool().submit(monitor);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		final JTextPane txtPane = new JTextPane();
		txtPane.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 13));
		txtPane.setEditable(false);
		scrollPane.setViewportView(txtPane);

		new FileLineModelDocumentAdaptor(txtPane.getDocument(), new FileLineModel(monitor));
	}

}
