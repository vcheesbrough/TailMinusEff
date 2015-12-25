package tailminuseff.fx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.inject.Inject;
import tailminuseff.io.FileMonitorListener;
import tailminuseff.io.FileResetEvent;
import tailminuseff.io.LineAddedEvent;

public class StyledLineModel {

    private final ReadOnlyListWrapper<StyledLine> lines = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private final UserSearchModel searchModel;

    @Inject
    public StyledLineModel(UserSearchModel searchModel) {
        this.searchModel = searchModel;
        this.searchModel.patternProperty().addListener(patternListener);
    }

    public ObservableList<StyledLine> getLines() {
        return lines.get();
    }

    public ReadOnlyListProperty<StyledLine> linesProperty() {
        return lines.getReadOnlyProperty();
    }

    public FileMonitorListener getMonitorListener() {
        return monitorListener;
    }

    private final ChangeListener<Pattern> patternListener = new ChangeListener<Pattern>() {
        @Override
        public void changed(ObservableValue<? extends Pattern> observable, Pattern oldValue, Pattern pattern) {
            synchronized (StyledLineModel.this) {
                if (pattern == null) {
                    for (int i = 0; i < lines.size(); i++) {
                        lines.set(i, lines.get(i).setStyledSegments(Collections.emptyList()));
                    }
                } else {
                    for (int i = 0; i < lines.size(); i++) {
                        final StyledLine oldLine = lines.get(i);
                        final StyledLine newLine = oldLine.applyStyle(pattern, searchModel.getStyle());
                        if (!newLine.equals(oldLine)) {
                            lines.set(i, newLine);
                        }
                    }
                }
            }
        }
    };

    private List<StyledLine.StyledSegment> applyPattern(Pattern pattern, String line) {
        List<StyledLine.StyledSegment> segments = new ArrayList<>();
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            segments.add(new StyledLine.StyledSegment(matcher.start(), matcher.end() - 1, searchModel.getStyle()));
        }
        return segments;
    }

    private final FileMonitorListener monitorListener = new FileMonitorListener() {
        @Override
        public void fileReset(FileResetEvent evt) {
            synchronized (StyledLineModel.this) {
                lines.clear();
            }
        }

        @Override
        public void lineRead(LineAddedEvent evt) {
            synchronized (StyledLineModel.this) {
                Pattern pattern = searchModel.getPattern();
                List<StyledLine.StyledSegment> segments
                        = pattern != null
                                ? applyPattern(pattern, evt.getLine())
                                : Collections.emptyList();
                lines.add(new StyledLine(lines.size(), evt.getLine(), segments));
            }
        }

    };
}
