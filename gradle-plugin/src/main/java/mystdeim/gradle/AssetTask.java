package mystdeim.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Roman Novikov
 */
public class AssetTask extends DefaultTask {

    static final String DESCRIPTION = "Compile static assets";

    Engine engine;

    public AssetTask() {
        this.engine = new Engine(
                "webroot",
                "build/resources/main",
                "webroot");
    }

    @TaskAction
    public void greet() throws Exception {
        AssetPipelinePluginExtension extension = getProject().getExtensions().findByType(AssetPipelinePluginExtension.class);
        if (extension == null) {
            extension = new AssetPipelinePluginExtension();
        }

        String message = extension.getMessage();
        System.out.println(message);

        engine.run();
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
