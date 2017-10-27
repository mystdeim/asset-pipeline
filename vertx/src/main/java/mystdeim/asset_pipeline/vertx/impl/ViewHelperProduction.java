package mystdeim.asset_pipeline.vertx.impl;

import mystdeim.asset_pipeline.commons.Const.Dir;
import mystdeim.asset_pipeline.vertx.ViewHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ViewHelperProduction implements ViewHelper {

    private final String webroot;
    private final String publicPath;

    public ViewHelperProduction() {
        this(Dir.WEB_ROOT, Dir.PUBLIC);
    }

    public ViewHelperProduction(String webroot, String publicPath) {
        this.webroot = webroot;
        this.publicPath = publicPath;
    }

    @Override
    public String css() throws FileNotFoundException {
        return String.format("<link rel='stylesheet' href='%s'/>", asset(Dir.APP_CSS));
    }

    @Override
    public String js() throws FileNotFoundException {
        return String.format("<script src='%s'></script>", asset(Dir.APP_JS));
    }

    public String asset(String name) throws FileNotFoundException {
        Properties properties = new Properties();
        try {
            String path = String.format("/%s", Dir.ASSETS_PROP);
            try (InputStream in = ViewHelperProduction.class.getResourceAsStream(path)) {
                properties.load(in);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String digest = properties.getProperty(name);
        if (null == digest) {
            throw new FileNotFoundException(name);
        } else {
            return String.format("/%s/%s", publicPath, properties.getProperty(name));
        }
    }
}
