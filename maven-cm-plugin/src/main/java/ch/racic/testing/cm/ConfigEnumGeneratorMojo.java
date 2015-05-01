package ch.racic.testing.cm;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(
        name = "generateConfigEnum",
        requiresProject = true,
        defaultPhase = LifecyclePhase.NONE
)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class ConfigEnumGeneratorMojo extends AbstractMojo {

    @Component
    protected MavenProject project;

    @Component
    private Settings settings;

    @Parameter(
            required = true,
            readonly = true,
            defaultValue = "${project.build.directory}/generated-sources/configEnums",
            property = "outputDir"
    )
    private File outputDir;

    @Parameter(
            required = true,
            readonly = true,
            defaultValue = "${project.groupId}",
            property = "basePackage"
    )
    private String basePackage;

    @Parameter(
            required = true,
            readonly = true,
            defaultValue = "false",
            property = "testSourceOnly"
    )
    private boolean testSourceOnly;

    public void execute() throws MojoExecutionException {
        // TODO generate something
        List<Resource> resources = project.getTestResources();
        resources.addAll(project.getResources());
        // getting root resource folders and check if there is a config folder to determine if there are any interesting files to parse?
        List<File> resourceDirs = parseResources(resources);


        // After all is generated, let's add it to the corresponding source root
        if (testSourceOnly) loadGeneratedTestSources();
        else loadGeneratedSources();

    }

    private List<File> parseResources(List<Resource> resources) {
        List<File> configFolders = new ArrayList<File>();
        // iterate over the resource folders and see which contain any config folder
        for (Resource res : resources) {
            File root = new File(res.getDirectory());
            File config = new File(root, "config");
            if (config.exists() && config.isDirectory()) {
                // Found a config folder
                configFolders.add(config);
            }
        }
        return configFolders;
    }

    private void loadGeneratedTestSources() {
        if (!settings.isInteractiveMode()) {
            getLog().info(String.format("Adding generated configuration enums from %s to test compile source root",
                    outputDir.getAbsolutePath()));
        }
        project.addTestCompileSourceRoot(outputDir.getAbsolutePath());
    }

    private void loadGeneratedSources() {
        if (!settings.isInteractiveMode()) {
            getLog().info(String.format("Adding generated configuration enums from %s to compile source root",
                    outputDir.getAbsolutePath()));
        }
        project.addTestCompileSourceRoot(outputDir.getAbsolutePath());
    }
}
