package mystdeim.asset_pipeline.commons;

/**
 * @author Roman Novikov
 */
public final class Const implements CommonConst {

    interface EnvConst {
        String DEV = "dev";
        String PROD = "prod";
    }

    interface DirConst {
        String PUBLIC = "public/";
        String CSS_HOME = "webroot/css";
        String JS_HOME = "webroot/js";
    }

    public static final class Env implements EnvConst {
        private Env() {}
    }
    public static final class Dir implements DirConst {
        private Dir() {}
    }
    private Const() {}

}
