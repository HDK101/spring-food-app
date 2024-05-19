package com.eisen.foodapp.module.food.model;

import com.eisen.foodapp.module.food.dto.CreateFoodDTO;
import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.model.User;
import jakarta.persistence.*;

@Entity(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foods_generator")
    @SequenceGenerator(name = "foods_generator", sequenceName = "foods_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;

    @Column(name = "price_in_cents")
    private Long priceInCents;

    public static Food from(CreateFoodDTO data) {
        Food food = new Food();
        food.setName(data.name());
        food.setPriceInCents(data.priceInCents());
        return food;
    }

    public void put(CreateFoodDTO data) {
        this.setName(data.name());
        this.setPriceInCents(data.priceInCents());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(Long priceInCents) {
        this.priceInCents = priceInCents;
    }
}
