package mystdeim.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Roman Novikov
 */
public class AssetPipeline implements Plugin<Project> {

    @Override
    public void apply(Project project) {
//        project.getExtensions().create("assetPipelineSetting", AssetPipelinePluginExtension.class);
        project.getTasks().create("compileAssets", AssetTask.class);

        AssetPipelinePluginExtension ext = new AssetPipelinePluginExtension();
        project.getDependencies().add("compile",
                String.format("io.vertx:vertx-jdbc-client:%s", ext.getVertxVersion()));

    }

}
