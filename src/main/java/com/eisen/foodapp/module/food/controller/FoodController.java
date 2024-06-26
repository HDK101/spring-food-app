package com.eisen.foodapp.module.food.controller;

import com.eisen.foodapp.module.food.dto.CreateFoodDTO;
import com.eisen.foodapp.module.food.model.Food;
import com.eisen.foodapp.module.food.repository.FoodRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("foods")
public class FoodController {
    @Autowired
    private FoodRepository foodRepository;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<Page<Food>> index(Pageable pageable) {
        return ResponseEntity.ok(this.foodRepository.findAll(pageable));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<Food> show(@PathVariable Long id) {
        Food food = foodRepository.findById(id).orElseThrow();

        return ResponseEntity.ok(food);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Food> store(@Valid @RequestBody CreateFoodDTO data) {
        Food food = Food.from(data);

        var savedFood = foodRepository.save(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFood);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public ResponseEntity<Food> update(@PathVariable Long id, @Valid @RequestBody CreateFoodDTO data) {
        Food food = foodRepository.findById(id).orElseThrow();
        food.put(data);

        var savedFood = foodRepository.save(food);
        return ResponseEntity.ok(savedFood);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public ResponseEntity delete(@PathVariable Long id) {
        if (!foodRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        foodRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
