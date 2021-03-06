package tailminuseff.fx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyledLine {

    private final int lineNumber;
    private final String text;
    private final List<StyledSegment> styledSegments;

    public StyledLine(int lineNumber, String text, List<StyledSegment> styledSegments) {
        this.lineNumber = lineNumber;
        this.text = text;
        this.styledSegments = Collections.unmodifiableList(styledSegments);
    }

    public StyledLine setStyledSegments(List<StyledSegment> styledSegments) {
        return new StyledLine(lineNumber, text, styledSegments);
    }

    public StyledLine applyStyle(Pattern pattern, InlineTextStyle style) {
        List<StyledLine.StyledSegment> segments = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            segments.add(new StyledLine.StyledSegment(matcher.start(), matcher.end() - 1, style));
        }
        return setStyledSegments(segments);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getText() {
        return text;
    }

    public List<StyledSegment> getStyledSegments() {
        return styledSegments;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.lineNumber;
        hash = 97 * hash + Objects.hashCode(this.text);
        hash = 97 * hash + Objects.hashCode(this.styledSegments);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StyledLine other = (StyledLine) obj;
        if (this.lineNumber != other.lineNumber) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.styledSegments, other.styledSegments)) {
            return false;
        }
        return true;
    }

    public static class StyledSegment {

        private final int firstCharacter;
        private final int lastCharacter;
        private final InlineTextStyle style;

        public StyledSegment(int firstCharacter, int lastCharacter, InlineTextStyle style) {
            this.firstCharacter = firstCharacter;
            this.lastCharacter = lastCharacter;
            this.style = style;
        }

        public int getFirstCharacter() {
            return firstCharacter;
        }

        public int getLastCharacter() {
            return lastCharacter;
        }

        public InlineTextStyle getStyle() {
            return style;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.firstCharacter;
            hash = 37 * hash + this.lastCharacter;
            hash = 37 * hash + Objects.hashCode(this.style);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final StyledSegment other = (StyledSegment) obj;
            if (this.firstCharacter != other.firstCharacter) {
                return false;
            }
            if (this.lastCharacter != other.lastCharacter) {
                return false;
            }
            if (!Objects.equals(this.style, other.style)) {
                return false;
            }
            return true;
        }
    }
}
