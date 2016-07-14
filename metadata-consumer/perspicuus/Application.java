package org.jboss.perspicuus;

import static net.logstash.logback.marker.Markers.append;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jboss.perspicuus.generated.GeneratedCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jhalli on 06/07/16.
 */
public class Application {

	public static void main(String[] args) throws Exception {

		sending();

		receiving();
	}

	private static void receiving() throws Exception {

		/*
		 * I'm a data science nerd. I want to analyse trends in new customer
		 * signups. It would be nice if we had a directory that told me where to
		 * find that event data, but I had to ask around and eventually someone
		 * told me about this file
		 */
		Stream<String> lines = Files.lines(new File("logs/analytics.log")
				.toPath());

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
			Stream<GeneratedCustomer> customers = SchemaUtils
					.newInstance()
					.streamFromSchema(records, "customer");
			customers.forEach(System.out::println);
			
		} catch (Exception e2) {
			// TODO: handle exception
		}


		/*
		 * yay, finally! now I can start doing my data analysis.
		 */

	}

	private static void sending() throws Exception {

		/*
		 * I'm a biz application developer. My app logic registers new users:
		 */
		CustomerBean customerBean = new CustomerBean(101, "A. Customer");

		/*
		 * Some egg-head data science people may want to know how many new users
		 * we have. Let me put in a hook to provide that information. I know
		 * nothing about analytics, but I can do logging...
		 */
		Logger logger = LoggerFactory.getLogger(Application.class);
		logger.info("customer_registered_event",
				append("customer", customerBean));

		/*
		 * Well that was pretty painless. The logging framework will spit out
		 * JSON, because that is what resources/logback.xml tells it to do. But
		 * to make it useful the receiver will need a schema for CustomerBean...
		 */
		ObjectMapper mapper= new ObjectMapper();
		//
		SchemaUtils.sendMsgFromEnv("customerstream", mapper.writeValueAsString(customerBean));

		// This logic will be moved
		SchemaUtils.persistSchema(CustomerBean.class.getCanonicalName(),
				"/tmp/schema.json");

		/*
		 * Wow, that was tedious. If only the framework had done all that for me
		 * by magic. Oh well.
		 */
	}

}
