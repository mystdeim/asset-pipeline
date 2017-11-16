package mystdeim.asset_pipeline.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Roman Novikov
 */
public class AssetPipeline implements Plugin<Project> {

    public static final String BUILD = "build";
    public static final String COMPILE_ASSETS = "compileAssets";

    @Override
    public void apply(Project project) {
//        project.getExtensions().create("assetPipelineSetting", AssetPipelinePluginExtension.class);
//        AssetPipelinePluginExtension ext = new AssetPipelinePluginExtension();

        project.getTasks().create(COMPILE_ASSETS, AssetTask.class);
//        project.getDependencies().add("compile",
//                String.format("com.github.mystdeim.asset-pipeline:vertx:+"));

        project.getTasks().getByName(BUILD).dependsOn(COMPILE_ASSETS);

//        project.getPlugins().apply("org.kordamp.gradle:livereload-gradle-plugin:0.2.1");
        project.getPluginManager().apply("org.kordamp.gradle.livereload");
    }

}
