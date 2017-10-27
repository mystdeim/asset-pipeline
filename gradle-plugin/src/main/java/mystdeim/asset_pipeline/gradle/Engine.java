package mystdeim.asset_pipeline.gradle;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Roman Novikov
 *
 * Source directory:
 * assets/
 *   css/app.css
 *   js/app.js
 *   public/
 *     logo.png
 *     other/
 *       img01.png
 *
 * Build directory:
 * build/resources/main/
 *   manifest.properties
 *   webroot/
 *     app-#md5hash#.css
 *     app-#md5hash#.js
 *     logo-#md5hash#.png
 *     other/
 *       img01-#md5hash#.png
 *
 */
public class Engine {

    public static final String APP = "app";
    public static final String CSS = "css";
    public static final String JS = "js";
    public static final String PUBLIC = "public";
    public static final String MANIFEST = "assets.properties";

    static final int MAX_LINE_LENGTH = 1_000_000;

    final String inputDir;
    final String outputDir;
    final String assetsName;

    public Engine(String inputDir, String outputDir, String assetsName) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.assetsName = assetsName;
    }

    void run() throws Exception {
        Map<String, String> manifestMap = manifest();

        // manifest
        String manifestPath = outputDir + "/" + MANIFEST;
        String manifestContent = manifest(manifestMap);
        Files.write(Paths.get(manifestPath), manifestContent.getBytes());
    }


    String css() throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(String.format("%s/%s/%s.css", inputDir, CSS, APP))));
        String fileName = String.format("%s-%s.css", APP, hash(content));

        String cssDir = String.format("%s/%s", outputDir, assetsName);
        new File(cssDir).mkdirs();
        String css_path = String.format("%s/%s", cssDir, fileName);

        try (Writer css_writer = new FileWriter(css_path)) {
            Reader reader = new StringReader(content);
            CssCompressor compressor = new CssCompressor(reader);
            compressor.compress(css_writer, MAX_LINE_LENGTH);
        }

        return fileName;
    }

    String js() throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(String.format("%s/%s/%s.js", inputDir, JS, APP))));
        String fileName = String.format("%s-%s.js", APP, hash(content));

        String js = String.format("%s/%s", outputDir, assetsName);
        new File(js).mkdirs();
        String js_path = String.format("%s/%s", js, fileName);

        try (Writer js_writer = new FileWriter(js_path)) {
            Reader reader = new StringReader(content);
            JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ErrorReporterImpl());
            compressor.compress(js_writer, MAX_LINE_LENGTH, true, true, true, false);
        }

        return fileName;
    }

    void public_assets(Map<String, String> manifest) throws IOException {
        Files.walk(Paths.get(inputDir, PUBLIC)).filter(Files::isRegularFile).forEach(path -> {
            try {
                byte[] bs = Files.readAllBytes(path);
                Path fileOriginal = path.getFileName();
                String fileName[] = fileOriginal.toString().split("\\.(?=[^\\.]+$)");
                String prodFile = String.format("%s-%s.%s", fileName[0], hash(bs), fileName[1]);

                Path parentPath = Paths.get(inputDir, PUBLIC).relativize(path).getParent();
                if (null == parentPath) {
                    manifest.put(fileOriginal.toString(), prodFile);
                    Paths.get(outputDir, assetsName).toFile().mkdirs();
                    Files.write(Paths.get(outputDir, assetsName, prodFile), bs);
                } else {
                    manifest.put(
                            parentPath + "/" + fileOriginal.toString(),
                            parentPath + "/" + prodFile);
                    Paths.get(outputDir, assetsName, parentPath.toString()).toFile().mkdirs();
                    Files.write(Paths.get(outputDir, assetsName, parentPath.toString(), prodFile), bs);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    Map<String, String> manifest() throws Exception {
        Map<String, String> manifestMap = new HashMap<>();

        manifestMap.put("app.css", css());
        manifestMap.put("app.js", js());
        public_assets(manifestMap);

        return manifestMap;
    }

    String manifest(Map<String, String> map) {
        return map.entrySet().stream()
                .map(e -> String.format("%s:%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }

    String hash(String content) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = content.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        String hash = new BigInteger(1, thedigest).toString(16);
        return hash;
    }

    String hash(byte[] bytesOfMessage) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        String hash = new BigInteger(1, thedigest).toString(16);
        return hash;
    }

    class ErrorReporterImpl implements ErrorReporter {
        @Override
        public void warning(String s, String s1, int i, String s2, int i1) {

        }

        @Override
        public void error(String s, String s1, int i, String s2, int i1) {

        }

        @Override
        public EvaluatorException runtimeError(String s, String s1, int i, String s2, int i1) {
            return null;
        }
    }
}
