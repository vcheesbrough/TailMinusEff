package tailminuseff;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class MultiFileModel {

    private final List<ActiveMonitor> models = new ArrayList<MultiFileModel.ActiveMonitor>();

    private final ExecutorService executorService;

    private FileLineModelFactory modelFactory;

    @Inject
    public MultiFileModel(FileLineModelFactory modelFactory, @FileExectutor ExecutorService executorService) {
        this.modelFactory = modelFactory;
        this.executorService = executorService;
    }

    public synchronized FileLineModel openFile(File newFile) {
        final FileLineModel model = modelFactory.createForFile(newFile);
        final Future<Void> future = executorService.submit(model.getFileMonitor());
        models.add(new ActiveMonitor(model, future));
        return model;
    }

    public synchronized void close(FileLineModel fileLineModel) throws InterruptedException, ExecutionException {
        final ActiveMonitor m = models.stream().filter(am -> am.getModel() == fileLineModel).findFirst().get();
        m.getFuture().cancel(true);
        try {
            m.getFuture().get();
        } catch (final CancellationException cex) { // this is expected

        }
        models.remove(m);
    }

    public synchronized FileLineModel close(File file) throws InterruptedException, ExecutionException {
        final ActiveMonitor m = models.stream().filter(am -> am.getModel().getFile().equals(file)).findFirst().get();
        m.getFuture().cancel(true);
        try {
            m.getFuture().get();
        } catch (final CancellationException cex) { // this is expected

        }
        models.remove(m);
        return m.getModel();
    }

    public synchronized List<File> getOpenFiles() {
        return models.stream().map(am -> am.getModel().getFile()).collect(Collectors.toList());
    }

    private class ActiveMonitor {

        public FileLineModel getModel() {
            return model;
        }

        public Future<Void> getFuture() {
            return future;
        }

        private final FileLineModel model;
        private final Future<Void> future;

        public ActiveMonitor(FileLineModel model, Future<Void> future) {
            super();
            this.model = model;
            this.future = future;
        }
    }
}
