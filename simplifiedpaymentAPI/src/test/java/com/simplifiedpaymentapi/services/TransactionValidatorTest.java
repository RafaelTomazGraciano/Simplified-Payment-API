package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.InsufficientFunds;
import com.simplifiedpaymentapi.exceptions.NotAuthorized;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class TransactionValidatorTest {

    @InjectMocks
    private TransactionValidator transactionValidator;

    @Test
    @DisplayName("Should throw NotAuthorized when user is a merchant")
    void shouldThrowNotAuthorizedWhenIsMerchant() {
        User merchantUser = new User();
        merchantUser.setUserType(UserType.MERCHANT);
        BigDecimal transactionValue = new BigDecimal("100");

        Assertions.assertThrows(NotAuthorized.class, () -> {
            transactionValidator.validateTransaction(merchantUser, transactionValue);
        });
    }

    @Test
    @DisplayName("Should throw InsufficientFunds when user has no balance")
    void shouldThrowInsufficientFundsWhenHasNoBalance() {
        User commonUser = new User();
        commonUser.setUserType(UserType.COMMON);
        commonUser.setBalance(new BigDecimal("50"));
        BigDecimal transactionValue = new BigDecimal("100");

        Assertions.assertThrows(InsufficientFunds.class, () -> {
            transactionValidator.validateTransaction(commonUser, transactionValue);
        });
    }

    @Test
    @DisplayName("Should validate Transaction, user is common and has enough balance")
    void validateTransactionSuccessfully() {
        User commonUser = new User();
        commonUser.setUserType(UserType.COMMON);
        commonUser.setBalance(new BigDecimal("200"));
        BigDecimal transactionValue = new BigDecimal("100");

        Assertions.assertDoesNotThrow(() -> {
            transactionValidator.validateTransaction(commonUser, transactionValue);
        });
    }
}
