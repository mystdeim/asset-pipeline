package mystdeim.asset_pipeline.gradle;

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
        engine.run();
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
