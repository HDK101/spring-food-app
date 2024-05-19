package com.eisen.foodapp;

import com.eisen.foodapp.module.food.dto.CreateFoodDTO;
import com.eisen.foodapp.module.food.model.Food;
import com.eisen.foodapp.module.food.repository.FoodRepository;
import com.eisen.foodapp.module.order.dto.CreateOrderDTO;
import com.eisen.foodapp.module.order.repository.OrderRepository;
import com.eisen.foodapp.module.user.dto.AuthenticationDTO;
import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.LoginResponseDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
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
@AutoConfigureMockMvc()
@ActiveProfiles("test")
public class OrderRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Food food1;

    private String currentToken;

    @BeforeEach
    public void beforeEach() throws Exception {
        food1 = Food.from(new CreateFoodDTO(
                "Food 1",
                1000L
        ));

        var user = User.from(
                new CreateUserDTO(
                        "Test user",
                        "user123",
                        "password"
                )
        );

        this.userRepository.save(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new AuthenticationDTO("user123", "password"));

        var response = mockMvc.perform(
                post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andReturn().getResponse();

        var dto = mapper.readValue(response.getContentAsString(), LoginResponseDTO.class);

        currentToken = dto.token();

        foodRepository.save(food1);
    }

    @AfterEach
    public void afterEach() {
        this.userRepository.deleteAll();
        this.foodRepository.deleteAll();
        this.orderRepository.deleteAll();
    }

    @ParameterizedTest()
    @MethodSource("provideFoodIds")
    public void testCreateOrders(List<Long> ids) throws Exception {
        System.out.println(currentToken);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new CreateOrderDTO(ids));

        mockMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + currentToken)
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
