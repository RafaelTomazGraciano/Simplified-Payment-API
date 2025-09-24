package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.dtos.UserDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.UserValidationException;
import com.simplifiedpaymentapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidatorService userValidatorService;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setup() {
        testUser1 = new User(1L, "Maria", "123", "123", "maria@gmail.com", new BigDecimal(100), UserType.COMMON);
        testUser2 = new User(2L, "John", "456", "456", "john@gmail.com", new BigDecimal(50), UserType.MERCHANT);
    }

    @Test
    @DisplayName("Should return a user when exist")
    void shouldReturnUserSuccessfully() throws Exception{
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        User foundUser = userService.findUserbyId(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw an exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound(){
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, ()->{
            userService.findUserbyId(2L);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository).findById(2L);
    }

    @Test
    @DisplayName("Should return a list of users when users exist")
    void shouldReturnListUsers(){
        when(userRepository.findAll()).thenReturn(List.of(testUser1, testUser2));
        List<User> userList = userService.getAllUsers();
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(testUser1, userList.get(0));
        assertEquals(testUser2, userList.get(1));
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no users exist")
    void shouldReturnVoidListUsers(){
        when(userRepository.findAll()).thenReturn(List.of());
        List<User> userList = userService.getAllUsers();
        assertNotNull(userList);
        assertEquals(0, userList.size());
        assertTrue(userList.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully(){
        UserDTO userDTO = new UserDTO("John", "123456", "12345678910", "12345", new BigDecimal(100), UserType.COMMON);
        User newUser = new User(userDTO);
        doNothing().when(userValidatorService).validateUser(any(User.class));

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(newUser.getDocument(), createdUser.getDocument());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        verify(userValidatorService).validateUser(any(User.class));
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("Should not save user if validation fails")
    void shouldNotSaveUserWhenValidationFails(){
        UserDTO userDTO = new UserDTO("John", "123456", "12345678910", "12345", new BigDecimal(100), UserType.COMMON);
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Email already exists");
        doThrow(new UserValidationException(errors))
                .when(userValidatorService).validateUser(any(User.class));

        Assertions.assertThrows(UserValidationException.class, ()->{
            userService.createUser(userDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

}