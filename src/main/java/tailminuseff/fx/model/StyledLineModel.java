package tailminuseff.fx.model;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tailminuseff.io.FileMonitorListener;
import tailminuseff.io.FileResetEvent;
import tailminuseff.io.LineAddedEvent;

public class StyledLineModel {

    private final ReadOnlyListWrapper lines = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    public ObservableList getLines() {
        return lines.get();
    }

    public ReadOnlyListProperty linesProperty() {
        return lines.getReadOnlyProperty();
    }

    public FileMonitorListener getMonitorListener() {
        return monitorListener;
    }

    private final FileMonitorListener monitorListener = new FileMonitorListener() {
        @Override
        public void fileReset(FileResetEvent evt) {
            lines.clear();
        }

        @Override
        public void lineRead(LineAddedEvent evt) {
            lines.add(evt.getLine());
        }
    };

}
