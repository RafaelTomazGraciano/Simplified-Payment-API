package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.UserValidationException;
import com.simplifiedpaymentapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserValidatorService userValidatorService;

    private User user;


    @BeforeEach
    void setup() {
        user = new User(1L, "Maria", "123", "123", "maria@gmail.com", new BigDecimal(100), UserType.COMMON);
    }

    @Test
    @DisplayName("Should validate user successfully")
    void shouldValidateUserSuccessfully(){
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByDocument(user.getDocument())).thenReturn(false);
        userValidatorService.validateUser(user);
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).existsByDocument(user.getDocument());
    }

    @Test
    @DisplayName("Should not validate user when email already exists")
    void shouldNotValidateUserWhenEmailExists(){
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(userRepository.existsByDocument(user.getDocument())).thenReturn(false);

        UserValidationException exception = assertThrows(UserValidationException.class, ()->{
            userValidatorService.validateUser(user);
        });

        Map<String, String> errors = exception.getErrors();
        assertNotNull(errors);
        assertTrue(errors.containsKey("email"));
        assertEquals("Email already exists", errors.get("email"));
        assertFalse(errors.containsKey("document"));
        assertNotEquals("Document already exists", errors.get("document"));
    }

    @Test
    @DisplayName("Should not validate user when document already exists")
    void shouldNotValidateUserWhenDocumentExists(){
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByDocument(user.getDocument())).thenReturn(true);

        UserValidationException exception = assertThrows(UserValidationException.class, ()->{
            userValidatorService.validateUser(user);
        });

        Map<String, String> errors = exception.getErrors();
        assertNotNull(errors);
        assertTrue(errors.containsKey("document"));
        assertEquals("Document already exists", errors.get("document"));
        assertFalse(errors.containsKey("email"));
        assertNotEquals("Email already exists", errors.get("email"));
    }

    @Test
    @DisplayName("Should not validate user when email and Document already exists")
    void shouldNotValidateUserWhenEmailAndDocumentExists(){
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(userRepository.existsByDocument(user.getDocument())).thenReturn(true);

        UserValidationException exception = assertThrows(UserValidationException.class, ()->{
            userValidatorService.validateUser(user);
        });

        Map<String, String> errors = exception.getErrors();
        assertNotNull(errors);
        assertTrue(errors.containsKey("email"));
        assertEquals("Email already exists", errors.get("email"));
        assertTrue(errors.containsKey("document"));
        assertEquals("Document already exists", errors.get("document"));
    }
}
