package com.eisen.foodapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/register")).andExpect(status().isCreated());
    }

    @Test
    public void testRegisterWithInvalidData() throws Exception {
        mockMvc.perform(post("/register")).andExpect(status().isCreated());
    }
}
