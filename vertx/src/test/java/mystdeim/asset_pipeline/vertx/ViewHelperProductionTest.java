package mystdeim.asset_pipeline.vertx;

import mystdeim.asset_pipeline.vertx.impl.ViewHelperProduction;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Roman Novikov
 */
public class ViewHelperProductionTest {

    ViewHelper viewHelper;
    Properties props;

    @Before
    public void setUp() throws IOException {
        viewHelper = new ViewHelperProduction("webroot_prod", "public");
        props = new Properties();
        try (InputStream in = ViewHelperProduction.class.getResourceAsStream("/assets.properties")) {
            props.load(in);
        }
    }

    @Test
    public void assetTest() throws Exception {
        String asset = viewHelper.asset("logo.png");
        assertEquals("/public/" + props.getProperty("logo.png"), asset);
    }

    @Test
    public void cssTest() throws Exception {
        String asset = viewHelper.css();
        String name = props.getProperty("app.css");
        assertEquals(String.format("<link rel='stylesheet' href='/public/%s'/>", name), asset);
    }

    @Test
    public void jsTest() throws Exception {
        String asset = viewHelper.js();
        String name = props.getProperty("app.js");
        assertEquals(String.format("<script src='/public/%s'></script>", name), asset);
    }

    @Test(expected = FileNotFoundException.class)
    public void assetNotFoundTest() throws Exception {
        viewHelper.asset("not_found");
    }
}
