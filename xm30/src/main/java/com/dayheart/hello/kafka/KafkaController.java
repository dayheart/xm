package com.dayheart.hello.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

	@Autowired
	private final KafkaProducer producer;
	
	
	public KafkaController(KafkaProducer producer) {
		this.producer = producer;
	}
	
	@PostMapping("/kafka1")
	public void sendMessage1(@RequestBody String message) {
        producer.sendMessage1(message);
    }
    
    @PostMapping("/kafka2")
    public void sendMessage2(@RequestBody String message) {
        producer.sendMessage2(message);
    }

}
