package io.fncode.jms.consumer;

import io.fncode.jpa.Order;
import io.fncode.jpa.OrderRepository;
import io.fncode.jpa.OrderState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository repository;

    @JmsListener(destination = "${spring.jms.template.default-destination}")
    public void receive(Message<Order> message) {
        log.info("receive(): message_id = '{}'", message.getHeaders().getId());

        this.processOrder(message.getPayload());
    }

    private void processOrder(Order order) {
        log.info("processOrder(): ref_id = '{}'", order.getReferenceId());

        this.preprocessOrder(order);
        this.persistOrder(order);
        this.postprocessOrder(order);
    }

    private void preprocessOrder(Order order) {
        log.info("preprocessOrder(): ref_id = '{}'", order.getReferenceId());

        if (order.getState().equals(OrderState.PREPROCESS_FAILURE)) {
            throw new IllegalArgumentException(String.format("pre-processing for ref_id = '%s' failed", order.getReferenceId()));
        }
    }

    private void persistOrder(Order order) {
        log.info("persistOrder(): ref_id = '{}'", order.getReferenceId());

        this.repository.save(order);
    }

    private void postprocessOrder(Order order) {
        log.info("postprocessOrder(): ref_id = '{}'", order.getReferenceId());

        if (order.getState().equals(OrderState.POSTPROCESS_FAILURE)) {
            throw new IllegalArgumentException(String.format("post-processing for ref_id = '%s' failed", order.getReferenceId()));
        }
    }
}
