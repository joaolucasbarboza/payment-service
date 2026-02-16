package com.joaobarboza.paymentservice.core.consumer;

import com.joaobarboza.paymentservice.core.PaymentService;
import com.joaobarboza.paymentservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.payment-success}"
    )
    public void consumeSuccessEvent(String payload) {
        log.info("Receiving success event from payment-success topic, payload:{}", payload);
        var event = jsonUtil.toEvent(payload);
        paymentService.realizePayment(event);
        log.info("Event success received from payment-success topic: {}", event.toString());
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.payment-fail}"
    )
    public void consumeFailEvent(String payload) {
        log.info("Receiving rollback event from payment-fail topic, payload:{}", payload);
        var event = jsonUtil.toEvent(payload);
        paymentService.realizeRefund(event);
        log.info("Event rollback received from payment-fail topic: {}", event);
    }
}
