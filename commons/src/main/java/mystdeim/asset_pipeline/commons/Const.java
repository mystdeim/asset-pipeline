package mystdeim.asset_pipeline.commons;

/**
 * @author Roman Novikov
 */
public final class Const  {

    interface EnvConst {
        String KEY = "env";
        String DEV = "dev";
        String PROD = "prod";
    }

    interface DirConst {
        String WEB_ROOT = "webroot";
        String WEB_STATIC = "static";
        String PUBLIC = "public";
        String CSS_HOME = "css";
        String JS_HOME = "js";
        String APP_CSS = "app.css";
        String APP_JS = "app.js";
        String ASSETS_PROP = "assets.properties";
    }

    public static final class Env implements EnvConst {
        private Env() {}
    }
    public static final class Dir implements DirConst {
        private Dir() {}
    }
    private Const() {}

}
