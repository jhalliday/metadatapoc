package org.jboss.perspicuus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.bzcareer.producer.KafkaMessenger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class SchemaUtils {

	private static final String DEFAULT_JAVA_GEN_LOCATION = "src/main/java";

	static public SchemaUtils newInstance() {
		// TODO Auto-generated method stub
		return new SchemaUtils();
	}

	public static String fromEnv(String env, String defaultValue) {
		try {
			String envValue = System.getenv(env);
			if (envValue != null)
				return envValue;
		} catch (Exception e) {
			System.err.println("Make sure to set the " + env
					+ " environment variable before running this program");
		}
		return null;
	}

	// export KAKFA_BROKER_LIST=192.168.99.100

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
		SchemaUtils.sendMsgFromEnv("schemastream",
				mapper.writeValueAsString(jsonSchema));
		Files.write(new File(saveLocation).toPath(),
				Collections.singletonList(schema));
	}

	public static void sendMsgFromEnv(String topic, String message) {
		KafkaMessenger messenger = new KafkaMessenger(SchemaUtils.fromEnv(
				"META_KAFKA_BROKERLIST", "localhost:9092"));

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
