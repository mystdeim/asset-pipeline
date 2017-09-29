package mystdeim.asset_pipeline.commons;

/**
 * @author Roman Novikov
 */
public enum DIRS {

    PUBLIC("public/"),
    CSS_HOME("webroot/css"),
    JS_HOME("webroot/js");

    private final String value;

    DIRS(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
