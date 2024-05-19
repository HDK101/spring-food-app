package com.eisen.foodapp;

import com.eisen.foodapp.module.food.dto.CreateFoodDTO;
import com.eisen.foodapp.module.food.model.Food;
import com.eisen.foodapp.module.food.repository.FoodRepository;
import com.eisen.foodapp.module.order.dto.CreateOrderDTO;
import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OrderRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;
    private Food food1;

    @BeforeEach
    public void beforeEach() {
        food1 = Food.from(new CreateFoodDTO(
                "Food 1",
                1000L
        ));

        foodRepository.save(food1);
    }

    @ParameterizedTest()
    @MethodSource("provideFoodIds")
    public void testCreateOrders(List<Long> ids) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new CreateOrderDTO(ids));

        mockMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());
    }

    private static Stream<Arguments> provideFoodIds() {
        return Stream.of(
                Arguments.of(List.of(1L)),
                Arguments.of(List.of(2L)),
                Arguments.of(List.of(3L)),
                Arguments.of(List.of(1L, 2L)),
                Arguments.of(List.of(3L, 2L)),
                Arguments.of(List.of(1L, 2L, 3L))
        );
    }
}
