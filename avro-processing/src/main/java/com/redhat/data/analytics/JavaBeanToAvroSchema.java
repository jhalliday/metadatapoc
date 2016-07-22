package com.redhat.data.analytics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.avro.Schema;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.redhat.data.analytics.model.CustomerBean;

public class JavaBeanToAvroSchema {

  public JavaBeanToAvroSchema() {
    Writer writer = null;
    try {
      // You can also choose to use the class name to generate your schema if the class doesn't yet
      // exist.
      // For example: toAvroSchema("com.redhat.data.analytics.model.CustomerBean");
      Schema avroSchema = toAvroSchema(CustomerBean.class);
      String asJson = avroSchema.toString(true);
    
      System.out.println("Schema: " + asJson);
      writer = new FileWriter("src/main/resources/schema.json");
      writer.write(asJson);

    } catch (ClassNotFoundException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          System.err.println("Error: Could not write avro schema to file.");;
          e.printStackTrace();
        }
      }
    }

  }

  /**
   * Method is responsible for generating an avro schema from a java pojo by providing the class
   * name
   * 
   * @param className
   * @return
   * @throws ClassNotFoundException
   * @throws JsonMappingException
   */
  public static Schema toAvroSchema(String className)
      throws ClassNotFoundException, JsonMappingException {
    Class<?> cl = Class.forName(className);
    AvroFactory factory = new AvroFactory();
    ObjectMapper mapper = new ObjectMapper(factory);
    AvroSchemaGenerator generator = new AvroSchemaGenerator();
    mapper.acceptJsonFormatVisitor(cl, generator);
    AvroSchema generatedSchema = generator.getGeneratedSchema();
    Schema avroSchema = generatedSchema.getAvroSchema();
    return avroSchema;
  }

  /**
   *  Method is responsible for generating an avro schema from a java pojo by providing the class. 
   *  This is to be used if the class already exists that you would like to convert into an avro schema
   *  
   * @param T
   * @return
   * @throws ClassNotFoundException
   * @throws JsonMappingException
   */
  public static Schema toAvroSchema(Class T) throws ClassNotFoundException, JsonMappingException {
    Class<?> cl = Class.forName(T.getCanonicalName());
    AvroFactory factory = new AvroFactory();
    ObjectMapper mapper = new ObjectMapper(factory);
    AvroSchemaGenerator generator = new AvroSchemaGenerator();
    mapper.acceptJsonFormatVisitor(cl, generator);
    AvroSchema generatedSchema = generator.getGeneratedSchema();
    Schema avroSchema = generatedSchema.getAvroSchema();
    return avroSchema;
  }


  public static void main(String[] args) {
    new JavaBeanToAvroSchema();

  }
}
