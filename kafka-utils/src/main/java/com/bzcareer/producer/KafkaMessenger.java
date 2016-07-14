package com.bzcareer.producer;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaMessenger {

	private static KafkaProducer<String, String> producer;
	private final Properties properties = new Properties();

	public KafkaMessenger(String servers) {
		properties.put("bootstrap.servers", servers);
		properties.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String,String>(properties);

	}
	
	
	public Future<RecordMetadata> send(String topic, String msg){
		ProducerRecord<String, String> data = new ProducerRecord<>(topic, msg);
        return producer.send(data); 
	}

	public Future<RecordMetadata> sendWithCallback(String topic, String msg){
		ProducerRecord<String, String> data = new ProducerRecord<>(topic, msg);
        return producer.send(data, new KafkaMessengerCallback()); 
	}

	public void close(){
        producer.close();
	}

}
