package org.jboss.perspicuus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jboss.perspicuus.generated.GeneratedCustomer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jhalli on 06/07/16.
 */
public class Application {

	public static void main(String[] args) throws Exception {

		receiving();
	}

	private static void receiving() throws Exception {

		/*
		 * I'm a data science nerd. I want to analyse trends in new customer
		 * signups. It would be nice if we had a directory that told me where to
		 * find that event data, but I had to ask around and eventually someone
		 * told me about this file
		 */

		Stream<String> lines = Files.lines(new File(fromEnv()).toPath());

		/*
		 * Hmm, ok, looks like JSON. I can totally do that...
		 */
		ObjectMapper mapper = new ObjectMapper();
		Stream<Map<String, Object>> records = lines
				.map(new Function<String, Map<String, Object>>() {
					@Override
					public Map<String, Object> apply(String s) {
						try {
							return mapper.readValue(s,
									new TypeReference<Map<String, Object>>() {
									});
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				});

		/*
		 * hmm, this sure would be easier if I knew a bit more about Customer
		 * object format. Good thing they gave me a schema too!
		 */

		try {
			Stream<GeneratedCustomer> customers = streamFromSchema(records, "customer");
			customers.forEach(System.out::println);

		} catch (Exception e2) {
			// TODO: handle exception
		}

		/*
		 * yay, finally! now I can start doing my data analysis.
		 */

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
    public static Stream<GeneratedCustomer> streamFromSchema(
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

	/**
	 * By default returns the location to  logs/analytics.log if environment variable is not set: 
	 * @return
	 */
	private static String fromEnv() {
		try {
			String logfolder = System.getenv("META_LOG_LOCATION");
			if (logfolder != null)
				return logfolder;
		} catch (Exception e) {
			System.err
					.println("Make sure to set the META_LOG_LOCATION environment variable before running this program");
		}
		return "logs/analytics.log";
	}
}
