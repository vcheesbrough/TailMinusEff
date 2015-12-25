package tailminuseff.fx.model;

public class InlineTextStyle {

    private final String foregroundCSSColour;
    private final String backgroundCSSColour;

    public InlineTextStyle() {
        foregroundCSSColour = null;
        backgroundCSSColour = null;
    }

    public InlineTextStyle(String foregroundCSSColour, String backgroundCSSColour) {
        this.foregroundCSSColour = foregroundCSSColour;
        this.backgroundCSSColour = backgroundCSSColour;
    }

    public String toCSS() {
        return (foregroundCSSColour != null ? ("-fx-fill: " + this.foregroundCSSColour + ";") : "")
                + (backgroundCSSColour != null ? ("-fx-background-color: " + backgroundCSSColour + ";") : "");
    }

}
