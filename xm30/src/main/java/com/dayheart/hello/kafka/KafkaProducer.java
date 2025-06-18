package com.dayheart.hello.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import com.dayheart.util.TierConfig;
import com.dayheart.util.XLog;

@Service
//@RequiredArgsConstructor
public class KafkaProducer {
	
	//@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private TierConfig tierConfig;

	// Not support @RequiredArgsConstructor
	//@Autowired
	public KafkaProducer() {
	}
	
	private void initKafkaTemplate() {
		Map<String, Object> configProps = new HashMap<>();
		//configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.producer.bootstrap-servers", "localhost:19092"));
		//configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.producer.bootstrap-servers")); // bootstrap.servers
		//configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.producer.bootstrap-servers", "localhost:9092"));
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
		XLog.stdout(String.format("spring.kafka.producer.bootstrap-servers[%s]", System.getProperty("spring.kafka.producer.bootstrap-servers")));
		XLog.stdout(String.format("KAFKA_PRODUCER_CONFIG[%s]", configProps));
		
		kafkaTemplate = new KafkaTemplate<>(producerFactory);
	}
	
	
	public void sendMessage1(String message) {
		if(kafkaTemplate==null) {
			initKafkaTemplate();
		}
    	System.out.println("Producer topic: topic1, message: " + message);
        kafkaTemplate.send("topic1", message);
    }
    
    public void sendMessage2(String message) {
    	if(kafkaTemplate==null) {
			initKafkaTemplate();
		}
    	System.out.println("Producer topic: topic2, message: " + message);
        kafkaTemplate.send("topic2", message);
    }
    
    public void sendMessage(String topicName, String message) {
    	if(kafkaTemplate==null) {
			initKafkaTemplate();
		}
    	//XLog.stdout(String.format("SEND esb_kafka_topic [%s]", message));
    	
    	//String topicName = tierConfig.getRole();
    	//String topicName = "esb_kafka_topic";
    	XLog.stdout(String.format("### SEND TOPIC[%s]", topicName));
    	
    	kafkaTemplate.send(topicName, message);
    }

}
