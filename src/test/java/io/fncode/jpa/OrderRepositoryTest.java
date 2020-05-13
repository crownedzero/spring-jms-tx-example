package io.fncode.jpa;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {

    @Autowired
    OrderRepository repository;

    @Test
    public void shouldSaveOrder() {
        /*@formatter:off*/
        this.repository.save(Order.builder()
                .referenceId(UUID.randomUUID())
                .state(OrderState.VALID_ORDER)
                .build());

        assertThat(this.repository.count()).isOne();
    }
}