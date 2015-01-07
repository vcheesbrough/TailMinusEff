package tailminuseff.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.io.File;
import java.util.concurrent.*;

import javax.swing.JTextPane;

import tailminuseff.*;
import java.awt.Font;

public class TestWindow {

	private JFrame frame;
	private final FileMonitor monitor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TestWindow window = new TestWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JTextPane txtPane = new JTextPane();
		txtPane.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 13));
		txtPane.setEditable(false);
		scrollPane.setViewportView(txtPane);

		new FileLineModelDocumentAdaptor(txtPane.getDocument(), new FileLineModel(monitor));
	}

}
