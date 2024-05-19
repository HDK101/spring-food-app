package com.eisen.foodapp;


import com.eisen.foodapp.module.user.dto.CreateUserDTO;
import com.eisen.foodapp.module.user.dto.RegisterDTO;
import com.eisen.foodapp.module.user.dto.SetRolesDTO;
import com.eisen.foodapp.module.user.model.Role;
import com.eisen.foodapp.module.user.model.User;
import com.eisen.foodapp.module.user.repository.RoleRepository;
import com.eisen.foodapp.module.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role1;
    private Role role2;
    private Role role3;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.from(
                new CreateUserDTO(
                        "Test user",
                        "user123",
                        "password"
                )
        );

        userRepository.save(user);

        role1 = this.roleRepository.save(new Role("Role1", "ROLE_1"));
        role2 = this.roleRepository.save(new Role("Role2", "ROLE_2"));
        role3 = this.roleRepository.save(new Role("Role3", "ROLE_3"));
    }

    @AfterEach
    public void afterEach() {
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    @ParameterizedTest()
    @MethodSource("provideRoles")
    public void testAssociateUserRoles(List<String> roles) throws Exception {
        var rolesIterable = this.roleRepository.findAllByRoleIn(roles);

        List<Long> roleIds = new ArrayList<>();
        rolesIterable.forEach(role -> roleIds.add(role.getId()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var body = mapper.writeValueAsString(new SetRolesDTO(roleIds));

        mockMvc.perform(
                put("/users/" + user.getId() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());

        var userFromDb = userRepository.findById(user.getId()).orElseThrow();
        userFromDb.getRoles().forEach(role -> Assert.isTrue(roles.contains(role.getRole()), "roles does not contain " + role.getRole()));
    }

    private static Stream<Arguments> provideRoles() {
        return Stream.of(
                Arguments.of(List.of("ROLE_1")),
                Arguments.of(List.of("ROLE_2")),
                Arguments.of(List.of("ROLE_3")),
                Arguments.of(List.of("ROLE_1", "ROLE_2")),
                Arguments.of(List.of("ROLE_2", "ROLE_3")),
                Arguments.of(List.of("ROLE_1", "ROLE_3")),
                Arguments.of(List.of("ROLE_1", "ROLE_2", "ROLE_3"))
        );
    }
}
