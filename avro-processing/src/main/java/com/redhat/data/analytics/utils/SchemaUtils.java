package com.redhat.data.analytics.utils;

import org.apache.avro.Schema;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.redhat.data.analytics.model.CustomerBean;

/**
 * Schema utils is a utility class used to generate files from Java Pojo and generate schema's as well as vice versa convert schema's into 
 * Java beans used to process data. 
 * @author Zak Hassan
 */
public class SchemaUtils {

  
  
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
  
  
  
  
  /**
   * Used to generate protocol buffer format from a java class.
   * @param T The class used to represent the schema of the data
   * @return
   * @throws JsonMappingException
   */
  public static NativeProtobufSchema toProtocolBuffer(Class T) throws JsonMappingException {
    ObjectMapper mapper = new ObjectMapper(new ProtobufFactory());
    ProtobufSchemaGenerator visitor = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(T , visitor);
    ProtobufSchema schemaWrapper = visitor.getGeneratedSchema();
    NativeProtobufSchema protocSchema = schemaWrapper.getSource();
    return protocSchema;
  }
  
  /**
   * See {@link #toProtocolBuffer(Class)} Provides same functionality except takes a string in case you don't have the class generated yet due to 
   * code generation utilities. This class is used to generate protocol buffer format from a java class. 
   * @param className A string representing the full class name used to represent the schema of the data
   * @return
   * @throws JsonMappingException
   */
  public static NativeProtobufSchema toProtocolBuffer(String className) throws JsonMappingException, ClassNotFoundException {
    Class<?> cl = Class.forName(className);
    ObjectMapper mapper = new ObjectMapper(new ProtobufFactory());
    ProtobufSchemaGenerator visitor = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(CustomerBean.class , visitor);
    
    ProtobufSchema schemaWrapper = visitor.getGeneratedSchema();
    NativeProtobufSchema nativeProtobufSchema = schemaWrapper.getSource();
    return nativeProtobufSchema;
  }
  
  
  
}
