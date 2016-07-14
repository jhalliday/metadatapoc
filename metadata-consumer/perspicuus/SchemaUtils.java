package org.jboss.perspicuus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jboss.perspicuus.generated.GeneratedCustomer;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;

import com.bzcareer.producer.KafkaMessenger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.sun.codemodel.JCodeModel;

public class SchemaUtils {

	private static final String DEFAULT_JAVA_GEN_LOCATION = "src/main/java";

	static public SchemaUtils newInstance() {
		// TODO Auto-generated method stub
		return new SchemaUtils();
	}

	 

	/**
	 * This will remain here and will be made to be more generic so anyone can
	 * generate a class with a given json schema file.
	 * 
	 * @param records
	 * @param schemaUri
	 * @param genClassName
	 * @param gPkgName
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Stream<GeneratedCustomer> streamFromSchema(
			Stream<Map<String, Object>> records, String schemaKey)
			throws MalformedURLException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Stream<GeneratedCustomer> customers = records
				.map(new Function<Map<String, Object>, GeneratedCustomer>() {
					@Override
					public GeneratedCustomer apply(Map<String, Object> record) {
						return mapper.convertValue(record.get(schemaKey),
								GeneratedCustomer.class);
					}
				});
		return customers;
	}

	
	

//	export KAKFA_BROKER_LIST=192.168.99.100
	
	/**
	 * This code will go to the encoder side and will be removed from the main
	 * application.
	 * 
	 * @param saveLocation
	 * @param className
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void persistSchema(String className, String saveLocation)
			throws JsonMappingException, JsonProcessingException, IOException,
			ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		mapper.acceptJsonFormatVisitor(
				mapper.constructType(Class.forName(className)), visitor);
		JsonSchema jsonSchema = visitor.finalSchema();
		String schema = mapper.writeValueAsString(jsonSchema);
		SchemaUtils.sendMsgFromEnv("schemastream", mapper.writeValueAsString(jsonSchema));
		Files.write(new File(saveLocation).toPath(),
				Collections.singletonList(schema));
	}
	
	public static void sendMsgFromEnv(String topic, String message){
		KafkaMessenger messenger = new KafkaMessenger("localhost:9092");

		try {
			messenger.sendWithCallback(topic, message).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.out.println("Error");
		} finally {
			messenger.close();
			System.out.println("Done!!");
		}
	}

}
