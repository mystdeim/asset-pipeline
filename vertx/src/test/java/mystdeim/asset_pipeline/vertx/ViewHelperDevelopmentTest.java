package mystdeim.asset_pipeline.vertx;

import io.vertx.core.json.JsonObject;
import mystdeim.asset_pipeline.commons.Const;
import mystdeim.asset_pipeline.vertx.impl.ViewHelperDevelopment;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Roman Novikov
 */
public class ViewHelperDevelopmentTest {

    ViewHelper viewHelper;

    @Before
    public void setUp() {
        viewHelper = new ViewHelperDevelopment() {
            @Override
            protected Path getPath(String path_txt) {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource(path_txt).getFile());
                return Paths.get(file.getAbsolutePath());
            }
        };
    }

    @Test
    public void assetTest() throws Exception {
        String asset = viewHelper.asset("logo.png");
        assertTrue("should starn with /public/logo.png?", asset.startsWith("/static/public/logo.png?"));
        assertTrue(asset.length() > "/static/public/logo.png?".length() + 5);
    }

    @Test
    public void cssTest() throws Exception {
        String asset = viewHelper.css();
        assertTrue(asset.startsWith("<link rel='stylesheet' href='/static/css/app.css?"));
        assertTrue(asset.trim().endsWith("'/>"));
        assertTrue(asset.length() > "<link rel='stylesheet' href='/static/css/app.css?".length() + 5);
    }

    @Test
    public void jsTest() throws Exception {
        String[] assets = viewHelper.js().split("\n");

        assertEquals("<script>window.uiEvents = [];</script>", assets[0]);
        assertEquals("<script src='//cdnjs.cloudflare.com/ajax/libs/require.js/2.3.3/require.min.js'></script>", assets[1]);
        assertEquals("<script src='http://localhost:35729/livereload.js?snipver=1'></script>", assets[2]);
        assertTrue(assets[3].startsWith("<script src='/static/js/app.js?"));
        assertTrue(assets[3].trim().endsWith("'></script>"));
        assertTrue(assets[3].length() > "<script src='/static/js/app.js?".length() + 5);
    }

    @Test(expected = FileNotFoundException.class)
    public void publicAssetNotFoundTest() throws Exception {
        viewHelper = new ViewHelperDevelopment("", "", "", "");
        viewHelper.asset("not_found.png");
    }
}
