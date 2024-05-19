package com.eisen.foodapp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest()
    @MethodSource("provideFoodIds")
    public void testAssociateUserRoles(List<Long> ids) throws Exception {
        mockMvc.perform(post("/orders")).andExpect(status().isCreated());
    }

    private static Stream<Arguments> provideFoodIds() {
        return Stream.of(
                Arguments.of(List.of(1, 2)),
                Arguments.of(List.of(2, 3)),
                Arguments.of(List.of(1))
        );
    }
}
