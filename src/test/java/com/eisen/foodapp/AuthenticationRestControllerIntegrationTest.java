package com.eisen.foodapp;

import com.eisen.foodapp.module.user.dto.AuthenticationDTO;
import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User currentUser;

    @BeforeEach
    public void beforeEach() {
        var user = User.from(
                new CreateUserDTO(
                        "Test user",
                        "user123",
                        "password"
                )
        );

        currentUser = user;

        this.userRepository.save(user);
    }

    @AfterEach
    public void afterEach() {
        this.userRepository.delete(currentUser);
    }

    @Test
    public void testAuthentication() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new AuthenticationDTO(
                "user123",
                "password"
        ));

        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated());
    }

    @Test
    public void testAuthenticationWithInvalidCredentials() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new AuthenticationDTO(
                "user1234",
                "password123"
        ));

        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().is4xxClientError());
    }
}
