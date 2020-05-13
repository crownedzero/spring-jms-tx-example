package io.fncode.api;

import io.fncode.jms.producer.OrderSender;
import io.fncode.jpa.Order;
import io.fncode.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderSender sender;

    private final JmsTemplate template;

    private final OrderRepository repository;

    @PostMapping
    public void sendOrders(@RequestBody List<Order> orders) {
        log.info("sendOrders()");
        this.sender.sendOrder(orders);
    }

    @GetMapping
    public List<Order> getOrders() {
        log.info("getOrders()");
        return this.repository.findAll();
    }

    @DeleteMapping
    public void deleteAllOrders() {
        log.info("deleteAllOrders()");
        this.repository.deleteAll();
    }


    @GetMapping("/count")
    public Integer messageCount(String queueName) {
        return this.template.browse(queueName, (session, browser) -> {
            Enumeration<?> messages = browser.getEnumeration();
            int total = 0;
            while (messages.hasMoreElements()) {
                messages.nextElement();
                total++;
            }
            log.info("messageCount(): queue: = '{}' total = '{}'", queueName, total);

            return total;
        });
    }}
