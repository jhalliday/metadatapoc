package com.redhat.data.analytics;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;

import com.redhat.data.analytics.generated.GeneratedCustomerBean;


public class StoreCustomerInfoInAvro {
  Schema schema;
  String avroSchemaFileName = "customer.avro";

  public StoreCustomerInfoInAvro() throws IOException {
    schema =
        new Schema.Parser().parse(getClass().getClassLoader().getResourceAsStream("customer.avsc"));
  }

  public void serialize() throws IOException {

    // Create new customers
    GenericRecord customer1 = createCustomer("Steve", 1);
    GenericRecord customer2 = createCustomer("Jeff", 2);
    GenericRecord customer3 = createCustomer("Justin", 3);

    // Serialize users
    File file = new File(avroSchemaFileName);
    DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
    DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
    dataFileWriter.create(schema, file);
    dataFileWriter.append(customer1);
    dataFileWriter.append(customer2);
    dataFileWriter.append(customer3);
    dataFileWriter.close();
    // Done!
    System.out.println("SUCCESS: Parsed Avro");

  }

  private GenericRecord createCustomer(String name, int id) {
    GenericRecord cust = new GenericData.Record(schema);
    cust.put("name", name);
    cust.put("id", id);
    return cust;
  }

  public void deserialize() throws IOException {
    File file = new File(avroSchemaFileName);
    DatumReader<GeneratedCustomerBean> customerDR =
        new SpecificDatumReader<GeneratedCustomerBean>(GeneratedCustomerBean.class);
    DataFileReader<GeneratedCustomerBean> dataFR =
        new DataFileReader<GeneratedCustomerBean>(file, customerDR);
    GeneratedCustomerBean customer = null;
    while (dataFR.hasNext()) {
      customer = dataFR.next(customer);
      System.out.println(customer);
    }
    dataFR.close();
  }

  public static void main(String[] args) throws IOException {
    StoreCustomerInfoInAvro poc = new StoreCustomerInfoInAvro();
    poc.serialize();
    poc.deserialize();
  }

}
