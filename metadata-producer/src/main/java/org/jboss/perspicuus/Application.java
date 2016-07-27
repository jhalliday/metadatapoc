package org.jboss.perspicuus;

import static net.logstash.logback.marker.Markers.append;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.data.analytics.utils.SchemaUtils;

/**
 * Created by jhalli on 06/07/16.
 */
public class Application {

  public static void main(String[] args) throws Exception {

    sending();

  }



  private static void sending() throws Exception {

    /*
     * I'm a biz application developer. My app logic registers new users:
     */
    CustomerBean customerBean = new CustomerBean(101, "A. Customer");

    /*
     * Some egg-head data science people may want to know how many new users we have. Let me put in
     * a hook to provide that information. I know nothing about analytics, but I can do logging...
     */
    Logger logger = LoggerFactory.getLogger(Application.class);
    logger.info("customer_registered_event", append("customer", customerBean));

    /*
     * Well that was pretty painless. The logging framework will spit out JSON, because that is what
     * resources/logback.xml tells it to do. But to make it useful the receiver will need a schema
     * for CustomerBean...
     */
    ObjectMapper mapper = new ObjectMapper();
    //

    SchemaUtils.newInstance().persistSchema(CustomerBean.class.getCanonicalName(),
        SchemaUtils.fromEnv("META_SCHEMA_LOCATION", "/tmp/schema.json"));

    /*
     * Wow, that was tedious. If only the framework had done all that for me by magic. Oh well.
     */
  }


}
