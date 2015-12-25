package tailminuseff.fx.model;

import java.util.regex.Pattern;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Singleton;

@Singleton
public class UserSearchModel {

    private final ObjectProperty<Pattern> pattern = new SimpleObjectProperty<>();

    public Pattern getPattern() {
        return pattern.get();
    }

    public void setPattern(Pattern value) {
        pattern.set(value);
    }

    public ObjectProperty<Pattern> patternProperty() {
        return pattern;
    }

    private final ObjectProperty<InlineTextStyle> style = new SimpleObjectProperty<>(new InlineTextStyle());

    public InlineTextStyle getStyle() {
        return style.get();
    }

    public void setStyle(InlineTextStyle value) {
        style.set(value);
    }

    public ObjectProperty<InlineTextStyle> styleProperty() {
        return style;
    }

}
