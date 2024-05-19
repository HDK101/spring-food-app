package com.eisen.foodapp.module.order.repository;

import com.eisen.foodapp.module.order.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
}
