package tailminuseff.swing;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.grapher.graphviz.GraphvizGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import tailminuseff.Guice3Module;
import tailminuseff.StackTraceDumpingEventBusConsumer;
import tailminuseff.config.Configuration;
import tailminuseff.swing.actions.ExitAction;
import tailminuseff.swing.actions.OpenFileAction;

@Singleton
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
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
                Injector injector = Guice3Module.CreateInjector();
                injector.getInstance(StackTraceDumpingEventBusConsumer.class);
                injector.getInstance(ErrorDisplayController.class);
                injector.getInstance(MainFrame.class).setVisible(true);
                graph("guice-graph.dot", injector);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static void graph(String filename, Injector demoInjector)
            throws IOException {
        PrintWriter out = new PrintWriter(new File(filename), "UTF-8");

        Injector injector = Guice.createInjector(new GraphvizModule());
        GraphvizGrapher grapher = injector.getInstance(GraphvizGrapher.class);
        grapher.setOut(out);
        grapher.setRankdir("TB");
        grapher.graph(demoInjector);
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
            tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel),
                    panel.getFileTabComponent());
        }
    };
    private final JTabbedPane tabbedPane;
    private final Provider<FileContentDisplayPanel> panelProvider;

    @SuppressWarnings("unused")
    @Inject
    public MainFrame(MultiFileModelSwingAdaptor multiFileModel,
            Configuration config, OpenFileAction openFileAction,
            ExitAction exitAction,
            Provider<FileContentDisplayPanel> panelProvider) {
        this.configuration = config;
        this.panelProvider = panelProvider;
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
