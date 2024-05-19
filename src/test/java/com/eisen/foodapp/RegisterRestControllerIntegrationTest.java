package com.eisen.foodapp;

import com.eisen.foodapp.module.user.dto.AuthenticationDTO;
import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.eisen.foodapp.module.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
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
public class RegisterRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void afterEach() {
        this.userRepository.deleteAll();
    }

    @Test
    public void testRegister() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new RegisterDTO(
                "Test user",
                "user123",
                "password"
        ));

        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    public void testRegisterWithInvalidLoginLength() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new RegisterDTO(
                "Test user",
                "us",
                "password"
        ));

        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void testRegisterWithInvalidPasswordLength() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new RegisterDTO(
                "Test user",
                "user123",
                "pass"
        ));

        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().is4xxClientError());
    }
}
