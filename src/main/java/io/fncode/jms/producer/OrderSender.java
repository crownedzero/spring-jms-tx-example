package io.fncode.jms.producer;

import io.fncode.jpa.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSender {

    private final JmsTemplate jmsTemplate;

    public void sendOrder(Order order) {
        this.jmsTemplate.convertAndSend(order, message -> {
            UUID messageId = UUID.randomUUID();

            log.info("sendOrder(): message_id = '{}'", messageId);

            message.setJMSMessageID(messageId.toString());

            return message;
        });
    }

    public void sendOrder(List<Order> orders) {
        orders.forEach(this::sendOrder);
    }
}
