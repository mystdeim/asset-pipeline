package mystdeim.asset_pipeline.vertx;

import io.vertx.core.json.JsonObject;
import mystdeim.asset_pipeline.commons.Const;
import mystdeim.asset_pipeline.vertx.impl.ViewHelperDevelopment;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
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
        viewHelper = new ViewHelperDevelopment();
    }

    @Test
    public void assetTest() throws Exception {
        String asset = viewHelper.asset("logo.png");
        assertTrue("should starn with /public/logo.png?", asset.startsWith("/public/logo.png?"));
        assertTrue(asset.length() > "/public/logo.png?".length() + 5);
    }

    @Test
    public void cssTest() throws Exception {
        String asset = viewHelper.css();
        assertTrue(asset.startsWith("<link rel='stylesheet' href='/css/app.css?"));
        assertTrue(asset.trim().endsWith("'/>"));
        assertTrue(asset.length() > "<link rel='stylesheet' href='/css/app.css?".length() + 5);
    }

    @Test
    public void jsTest() throws Exception {
        String asset = viewHelper.js();
        assertTrue(asset.startsWith("<script src='/js/app.js?"));
        assertTrue(asset.trim().endsWith("'></script>"));
        assertTrue(asset.length() > "<script src='/js/app.js?".length() + 5);
    }

    @Test(expected = FileNotFoundException.class)
    public void publicAssetNotFoundTest() throws Exception {
        viewHelper = new ViewHelperDevelopment("", "", "", "");
        viewHelper.asset("not_found.png");
    }
}
