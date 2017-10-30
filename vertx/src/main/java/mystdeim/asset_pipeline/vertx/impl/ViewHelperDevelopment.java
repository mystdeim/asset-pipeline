package mystdeim.asset_pipeline.vertx.impl;

import mystdeim.asset_pipeline.commons.Const.Dir;
import mystdeim.asset_pipeline.vertx.ViewHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewHelperDevelopment implements ViewHelper {

    private final String webRootPath;
    private final String cssPath;
    private final String jsPath;
    private final String publicPath;

    public ViewHelperDevelopment() {
        this(Dir.WEB_ROOT, Dir.CSS_HOME, Dir.JS_HOME, Dir.PUBLIC);
    }

    public ViewHelperDevelopment(String webRootPath, String cssPath, String jsPath, String publicPath) {
        this.webRootPath = webRootPath;
        this.cssPath = cssPath;
        this.jsPath = jsPath;
        this.publicPath = publicPath;
    }

    @Override
    public String css() throws FileNotFoundException {
        String viewedFile =  asset(Dir.APP_CSS, webRootPath, cssPath);
        return String.format("<link rel='stylesheet' href='%s'/>", viewedFile);
    }

    @Override
    public String js() throws FileNotFoundException {
        String viewedFile =  asset(Dir.APP_JS, webRootPath, jsPath);
        return String.format("<script src='%s'></script>", viewedFile);
    }

    @Override
    public String asset(String name) throws FileNotFoundException {
        return asset(name, webRootPath, publicPath);
    }

    // Helpers
    // -----------------------------------------------------------------------------------------------------------------

    String asset(String name, String webRoot, String subDir) throws FileNotFoundException {
        String file = String.format("%s/%s/%s", webRoot, subDir, name);
        String viewedFile = String.format("/%s/%s/%s", Dir.WEB_STATIC, subDir, name);
        return String.format("%s?%s", viewedFile, getTimestamp(file));
    }

    String getTimestamp(String path_txt) throws FileNotFoundException {
        Path path = getPath(path_txt);
        if (!Files.isRegularFile(path)) {
            throw new FileNotFoundException(path_txt);
        }
        try {
            return String.valueOf(Files.getLastModifiedTime(path).toMillis());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Path getPath(String path_txt) {
        return Paths.get(path_txt);
    }
}
