package mystdeim.asset_pipeline.gradle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Roman Novikov
 */
public class EngineTest {

    static final String ASSETS_DIR_NAME = "mystdeim/webroot";

    @Rule
    public TemporaryFolder buildFolder = new TemporaryFolder();

    Engine engine;

    @Before
    public void setUp() {
        engine = new Engine("src/test/resources/mystdeim/webroot", buildFolder.getRoot().toString(), ASSETS_DIR_NAME);
    }

    @Test
    public void testCss() throws Exception {
        String cssProductionFile = engine.css();

        assertEquals(1, buildFolder.getRoot().listFiles().length);

        // check file inside assets dir
        Path assetsPath = Paths.get(buildFolder.getRoot().toString(), ASSETS_DIR_NAME);
        assertEquals(1, Files.list(assetsPath).count());
        String cssFileName = Files.list(assetsPath).findAny().get().getFileName().toString();
        assertEquals(cssProductionFile, cssFileName);
        assertTrue(cssProductionFile.startsWith(Engine.APP));
        assertTrue(cssProductionFile.endsWith(".css"));

        // app-8b20aeff02af61f1be184f033efd622d.css
        assertEquals(40, cssProductionFile.length());

        // check content
        List<String> lines = Files.readAllLines(buildFolder.getRoot().toPath()
                .resolve(ASSETS_DIR_NAME).resolve(cssProductionFile));
        assertEquals(1, lines.size());
    }

    @Test
    public void testJs() throws Exception {
        String jsProductionFile = engine.js();

        assertEquals(1, buildFolder.getRoot().listFiles().length);

        // check file inside assets dir
        Path assetsPath = Paths.get(buildFolder.getRoot().toString(), ASSETS_DIR_NAME);
        assertEquals(1, Files.list(assetsPath).count());
        String jsFileName = Files.list(assetsPath).findAny().get().getFileName().toString();
        assertEquals(jsProductionFile, jsFileName);
        assertTrue(jsProductionFile.startsWith(Engine.APP));
        assertTrue(jsProductionFile.endsWith(".js"));

        // app-8b20aeff02af61f1be184f033efd622d.js
        assertEquals(39, jsProductionFile.length());

        // check content
        List<String> lines = Files.readAllLines(buildFolder.getRoot().toPath()
                .resolve(ASSETS_DIR_NAME).resolve(jsProductionFile));
        assertEquals(1, lines.size());
    }

    @Test
    public void testPublic() throws Exception {
        Map<String, String> map = new HashMap<>();
        engine.public_assets(map);

        // check manifest
        assertEquals(3, map.size());
        {
            String prodName = map.get("logo.png");
            assertTrue(prodName.startsWith("logo-"));
            assertTrue(prodName.endsWith(".png"));
            assertEquals(41, prodName.length());

            Path filePath = buildFolder.getRoot().toPath().resolve(ASSETS_DIR_NAME).resolve(prodName);
            assertTrue(Files.exists(filePath));
        }
        {
            String prodName = map.get("others/photo.jpg");
            assertTrue(prodName.startsWith("others/photo-"));
            assertTrue(prodName.endsWith(".jpg"));
            assertEquals(49, prodName.length());

            Path filePath = buildFolder.getRoot().toPath().resolve(ASSETS_DIR_NAME).resolve(prodName);
            assertTrue(Files.exists(filePath));
        }
        {
            String prodName = map.get("others/gallery/photo1.jpg");
            assertTrue(prodName.startsWith("others/gallery/photo1-"));
            assertTrue(prodName.endsWith(".jpg"));
            assertEquals(58, prodName.length());

            Path filePath = buildFolder.getRoot().toPath().resolve(ASSETS_DIR_NAME).resolve(prodName);
            assertTrue(Files.exists(filePath));
        }
    }

    @Test
    public void testRun() throws Exception {
        engine.run();

        long fileInRoor = buildFolder.getRoot().listFiles().length;
        long allFiles = Files.walk(buildFolder.getRoot().toPath()).parallel().filter(p -> !p.toFile().isDirectory()).count();
        assertEquals(2, fileInRoor);
        assertEquals(6, allFiles);

        // check manifest
        Properties manifest = new Properties();
        Path manifestPath = buildFolder.getRoot().toPath().resolve(Engine.MANIFEST);
        manifest.load(new FileInputStream(manifestPath.toFile()));

        // check css
        String cssFile = manifest.getProperty(Engine.APP + ".css");
        assertTrue(cssFile.startsWith(Engine.APP));
        assertTrue(cssFile.endsWith(".css"));
        assertEquals(40, cssFile.length());
    }

    @Test
    public void testManifest() throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("app.css", "app-#hash#.css");
        map.put("app.js", "app-#hash#.js");

        String[] content = engine.manifest(map).split(System.getProperty("line.separator"));
        assertEquals(2, content.length);
        assertEquals("app.css:app-#hash#.css", content[0]);
        assertEquals("app.js:app-#hash#.js", content[1]);
    }

    @Test
    public void compareProps() throws Exception {
        Properties example = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("mystdeim/assets.properties")) {
            example.load(resourceStream);
        }

        engine.run();
        Properties manifest = new Properties();
        Path manifestPath = buildFolder.getRoot().toPath().resolve(Engine.MANIFEST);
        manifest.load(new FileInputStream(manifestPath.toFile()));

        assertEquals(example, manifest);
    }

}
