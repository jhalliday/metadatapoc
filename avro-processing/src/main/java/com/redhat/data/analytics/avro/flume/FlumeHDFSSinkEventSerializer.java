package com.redhat.data.analytics.avro.flume;

import org.apache.flume.serialization.AbstractAvroEventSerializer;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.Schema;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.EventSerializer;

/**
 * Data pipeline may use Flume and I am evaluating it as a tool to transport data to from hadoop/kafka 
 * using avro format
 * 
 * @author Zak Hassan <zak.hassan@redhat.com>
 */
public class FlumeHDFSSinkEventSerializer extends AbstractAvroEventSerializer<Event> {

  private static final String CUSTOMER_AVSC = "customer.avsc";
  private final OutputStream out;

  public FlumeHDFSSinkEventSerializer(OutputStream out) {
    this.out = out;
  }
  
  public static class Builder implements EventSerializer.Builder {

    @Override
    public EventSerializer build(Context context, OutputStream out) {
      FlumeHDFSSinkEventSerializer flumeAvroWriter = new FlumeHDFSSinkEventSerializer(out);
      flumeAvroWriter.configure(context);
      return flumeAvroWriter;
    }

  }

  @Override
  protected Event convert(Event event) {
    return event;
  }

  @Override
  protected OutputStream getOutputStream() {
    return out;
  }

  @Override
  protected Schema getSchema() {
    Schema schema = null;
    try {
      schema = new Schema.Parser()
          .parse(getClass().getClassLoader().getResourceAsStream(CUSTOMER_AVSC));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return schema;
  }

}
