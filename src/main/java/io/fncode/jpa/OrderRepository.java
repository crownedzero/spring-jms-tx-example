package io.fncode.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Override
    <S extends Order> List<S> saveAll(Iterable<S> orders);

    @Override
    List<Order> findAll();

    @Override
    List<Order> findAllById(Iterable<Integer> ids);
}
