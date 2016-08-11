package com.bzcareer.producer;

import java.util.concurrent.ExecutionException;

import com.redhat.analytics.producer.KafkaMessenger;

public class Main {

	public static void main(String[] args) {
		KafkaMessenger messenger = new KafkaMessenger("localhost:9092");

		try {

			messenger.sendWithCallback("logtest", "----").get();
			messenger.sendWithCallback("logtest", "apple").get();
			messenger.sendWithCallback("logtest", "orange").get();
			messenger.sendWithCallback("logtest", "watermelon").get();
			messenger.sendWithCallback("logtest", "grape").get();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.out.println("Error");
		} finally {
			messenger.close();
		}
	}

}
