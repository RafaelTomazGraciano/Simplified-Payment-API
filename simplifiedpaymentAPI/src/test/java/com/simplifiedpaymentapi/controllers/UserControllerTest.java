package com.simplifiedpaymentapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifiedpaymentapi.dtos.UserDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.UserValidationException;
import com.simplifiedpaymentapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    UserDTO userDTO;

    @BeforeEach
    void setup() {
        userDTO = new UserDTO("Test", "123456", "123456578910", "test@gmail.com", new BigDecimal(10), UserType.COMMON);
    }

    @Test
    @DisplayName("Should crete a user and return status 201")
    void creteUserSuccessfully() throws Exception {
        User newUser = new User(userDTO);
        when(userService.createUser(any(UserDTO.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value("123456578910"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when validation fails")
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Email already exists");

        when(userService.createUser(any(UserDTO.class))).thenThrow(new UserValidationException(errors));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email already exists"));
    }

    @Test
    @DisplayName("Should return a list of users status 200")
    void shouldReturnListOfUsers() throws Exception {
        User user1 = new User(1L, "Maria", "123456789", "123", "maria@email.com", new BigDecimal(100), UserType.COMMON);
        User user2 = new User(2L, "John", "987654321", "456", "john@email.com", new BigDecimal(200), UserType.MERCHANT);
        List<User> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("Should return an empty list and status 200")
    void shouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

}
