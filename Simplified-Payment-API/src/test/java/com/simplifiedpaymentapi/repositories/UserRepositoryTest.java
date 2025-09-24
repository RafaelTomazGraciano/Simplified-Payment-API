package com.simplifiedpaymentapi.repositories;

import com.simplifiedpaymentapi.dtos.UserDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private User creteUser(UserDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }

    @Test
    @DisplayName("Should return true when document exists in the database")
    void shouldReturnTrueWhenDocumentExists() {
        String document = "123456578910";
        UserDTO data = new UserDTO("Rafael Tomaz Graciano", "123456", document, "test@gmail.com", new BigDecimal(10), UserType.COMMON);
        creteUser(data);

        boolean result = userRepository.existsByDocument(document);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when document does not exist in the database")
    void shouldReturnFalseWhenDocumentDoesntExist() {
        boolean result = userRepository.existsByDocument("000000000000");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when email exists in the database")
    void shouldReturnTrueWhenEmailExists() {
        String email = "email@gmail.com";
        UserDTO data = new UserDTO("Rafael Tomaz Graciano", "123456", "123456578910", email, new BigDecimal(10), UserType.COMMON);
        creteUser(data);

        boolean result = userRepository.existsByEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist in the database")
    void shouldReturnTrueWhenEmailDoesntExists() {
        boolean result = userRepository.existsByEmail("newEmail@gmail.com");
        assertThat(result).isFalse();
    }

}