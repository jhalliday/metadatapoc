package com.redhat.data.analytics.protoc;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.redhat.data.analytics.model.CustomerBean;
import com.redhat.data.analytics.utils.SchemaUtils;

public class ProtocolBufferProcessor {

  public static void main(String[] args) throws JsonMappingException {
    NativeProtobufSchema schema = SchemaUtils.toProtocolBuffer(CustomerBean.class);
    System.out.println(schema.toString());
  }

  
}
