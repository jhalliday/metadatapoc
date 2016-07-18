package org.jboss.perspicuus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;

import com.sun.codemodel.JCodeModel;

@Mojo(name = "codegen", defaultPhase = LifecyclePhase.COMPILE)
public class CodeGenMojo extends AbstractMojo {
	private static final String DEFAULT_JAVA_GEN_LOCATION = "src/main/java";

	/**
	 * If schema URI isn't provided throw an exception
	 */
	@Parameter(property = "schemaurl", required = true)
	private String schemaUrl;
	@Parameter(property = "classname", defaultValue = "SimpleClass", required = true)
	private String className;
	@Parameter(property = "packagename", defaultValue = "com.example", required = true)
	private String pkgName;
	@Parameter(defaultValue = "${basedir}/src/main/resources", property = "outputDir", required = true)
	private File outputDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("~~~~~~~~~~~~~~~~~~~~~~~");
		getLog().info("schemaUrl: " + schemaUrl);
		getLog().info("className: " + className);
		getLog().info("pkgName: " + pkgName);

		getLog().info("outputDirectory: " + outputDirectory);
		getLog().info(
				"outputDirectory: " + outputDirectory.getAbsolutePath()
						+ "/schema.json");
		getLog().info("HELLO FROM MAVEN!!!!!!!!!!");
		getLog().info("~~~~~~~~~~~~~~~~~~~~~~~");
		try {
			generateBeans(schemaUrl, className, pkgName);
		} catch (MalformedURLException e) {
			getLog().info(
					"Error: Unable to generate pojo from schema. For details see: "
							+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getLog().info(
					"Error: Unable to generate pojo from schema. For details see: "
							+ e.getMessage());
		}
	}

	private static String getFileName(String s) {
		List<String> split = Arrays.asList(s.split(":"));
		return split.get(split.size()-1);
	}
	
	
	/**
	 * This is logic to generate the java beans I'll be creating a maven plugin
	 * to generate the pojos
	 * 
	 * @param schemaUri
	 * @param genClassName
	 * @param gPkgName
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void generateBeans(String schemaUri, String genClassName,
			String gPkgName) throws MalformedURLException, IOException {
		URL schemaFile = null;
		if (schemaUri.startsWith("classpath:")) {
			
			schemaFile = Paths
					.get(outputDirectory.getAbsolutePath() + getFileName(schemaUri))
					.toUri().toURL();
		} else {
			schemaFile = new URL(schemaUri);
		}
		JCodeModel codeModel = new JCodeModel();
		GenerationConfig config = new DefaultGenerationConfig();
		SchemaMapper schemaMapper = new SchemaMapper(new RuleFactory(config,
				new Jackson2Annotator(), new SchemaStore()),
				new SchemaGenerator());
		schemaMapper.generate(codeModel, genClassName, gPkgName, schemaFile);
		codeModel.build(new File(DEFAULT_JAVA_GEN_LOCATION));

	}

	public void WriteSchemaFile() throws MojoExecutionException {
		File f = outputDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		File touch = new File(f, "touch.txt");

		FileWriter w = null;
		try {
			w = new FileWriter(touch);

			w.write("touch.txt");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + touch, e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
