package com.cybersoft.shop.messaging;

import com.cybersoft.shop.messaging.event.SignUpSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpEventPublisher {

    private final KafkaTemplate<String, SignUpSuccessEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.user-signup-success}")
    private String topic;

    public void publish(SignUpSuccessEvent event) {
        kafkaTemplate.send(topic, event.getEmail(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.err.println("[KAFKA] Publish failed: " + ex.getMessage());
                    } else {
                        RecordMetadata m = result.getRecordMetadata();
                        System.out.printf("[KAFKA] Published to %s-%d@%d%n",
                                m.topic(), m.partition(), m.offset());
                    }
                });
    }
}
