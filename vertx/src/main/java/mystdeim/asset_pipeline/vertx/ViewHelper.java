package mystdeim.asset_pipeline.vertx;

import io.vertx.core.json.JsonObject;
import mystdeim.asset_pipeline.commons.DIRS;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ViewHelper {

    JsonObject conf;

    // TODO: temporary!
    public ViewHelper() {
    }

    public ViewHelper(JsonObject conf) {
        this.conf = conf;
    }

    public String assets() {
        if (conf.getString("environment").equals("production")) {
            return String.format("<link rel='stylesheet' href='%s'", getProdAsset("app.css"));
        } else {
            return String.format("%s%n%s%n", getCssDev(), getJsDev());
        }
    }

    public String assets(String name) {
        if (conf.getString("environment").equals("production")) {
            return getProdAsset(name);
        } else {
            String file = DIRS.PUBLIC.getValue() + name;
            return String.format("%s?%s", file, getTimestamp("webroot/" + file));
        }
    }

    public String getTimestamp(String path) {
        try {
            return String.valueOf(Files.getLastModifiedTime(Paths.get(path)).toMillis());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getProdAsset(String name) {
        try {
//            JsonObject manifest = new JsonObject(new String(Files.readAllBytes(Paths.get("assets.json"))));
            Properties properties = new Properties();
            try (InputStream in = ViewHelper.class.getResourceAsStream("/assets.properties")) {
                properties.load(in);
            }
            return "/" + properties.getProperty(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getCssDev() {
        String path = "app.css";
        return String.format("<link rel='stylesheet' href='/css/%s?%s'>", path, getTimestamp(DIRS.CSS_HOME.getValue() + "/" + path));
    }

    String getJsDev() {
        String path = "js.css";
        return String.format(" <script src='%s'></script>", path, getTimestamp(DIRS.JS_HOME.getValue() + "/" + path));
    }

}
