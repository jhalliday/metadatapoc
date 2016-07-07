package org.jboss.perspicuus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.sun.codemodel.JCodeModel;
import org.jboss.perspicuus.generated.GeneratedCustomer;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.logstash.logback.marker.Markers.append;

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
        I'm a data science nerd. I want to analyse trends in new customer signups.
        It would be nice if we had a directory that told me where to find that event data,
          but I had to ask around and eventually someone told me about this file
         */
        Stream<String> lines = Files.lines(new File("logs/analytics.log").toPath());

        /*
        Hmm, ok, looks like JSON. I can totally do that...
         */
        ObjectMapper mapper = new ObjectMapper();
        Stream<Map<String,Object>> records = lines.map(new Function<String, Map<String,Object>>() {
            @Override
            public Map<String, Object> apply(String s) {
                try {
                    return mapper.readValue(s, new TypeReference<Map<String, Object>>() {});
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

        /*
        hmm, this sure would be easier if I knew a bit more about Customer object format.
        Good thing they gave me a schema too!
         */
        URL schemaFile = new URL("file:///tmp/schema.json");

        JCodeModel codeModel = new JCodeModel();
        GenerationConfig config = new DefaultGenerationConfig();
        SchemaMapper schemaMapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(), new SchemaStore()), new SchemaGenerator());
        schemaMapper.generate(codeModel, "GeneratedCustomer", "org.jboss.perspicuus.generated", schemaFile);
        codeModel.build(new File("src/main/java"));


        Stream<GeneratedCustomer> customers = records.map(new Function<Map<String, Object>, GeneratedCustomer>() {
            @Override
            public GeneratedCustomer apply(Map<String, Object> record) {
                return mapper.convertValue(record.get("customer"), GeneratedCustomer.class);
            }
        });

        customers.forEach(System.out::println);

        /*
        yay, finally! now I can start doing my data analysis.
         */

    }

    private static void sending() throws Exception {

        /*
        I'm a biz application developer. My app logic registers new users:
         */
        CustomerBean customerBean = new CustomerBean(101, "A. Customer");

        /*
        Some egg-head data science people may want to know how many new users we have.
        Let me put in a hook to provide that information.
        I know nothing about analytics, but I can do logging...
         */
        Logger logger = LoggerFactory.getLogger(Application.class);
        logger.info("customer_registered_event", append("customer", customerBean));

        /*
        Well that was pretty painless.
        The logging framework will spit out JSON, because that is what resources/logback.xml tells it to do.
        But to make it useful the receiver will need a schema for CustomerBean...
         */

        ObjectMapper mapper = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        mapper.acceptJsonFormatVisitor(mapper.constructType(CustomerBean.class), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();

        String schema = mapper.writeValueAsString(jsonSchema);
        Files.write(new File("/tmp", "schema.json").toPath(), Collections.singletonList(schema));

        /*
        Wow, that was tedious. If only the framework had done all that for me by magic. Oh well.
         */
    }
}
