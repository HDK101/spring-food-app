package com.eisen.foodapp.module.order.controller;

import com.eisen.foodapp.module.food.repository.FoodRepository;
import com.eisen.foodapp.module.order.dto.CreateOrderDTO;
import com.eisen.foodapp.module.order.model.Order;
import com.eisen.foodapp.module.order.repository.OrderRepository;
import com.eisen.foodapp.module.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Order>> all(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAll(pageable));
    }

    @GetMapping("/client")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Order>> allByClient() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(orderRepository.findAllByUserId(user.getId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Order> store(@RequestBody CreateOrderDTO data) {
        var foods = foodRepository.findAllById(data.foodIds());
        var order = new Order();

        foods.forEach(order::addFood);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUser(user);

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
