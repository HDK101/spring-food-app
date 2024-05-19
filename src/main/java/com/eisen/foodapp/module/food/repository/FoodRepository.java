package com.eisen.foodapp.module.food.repository;

import com.eisen.foodapp.module.food.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodRepository extends PagingAndSortingRepository<Food, Long>, CrudRepository<Food, Long> {
}
