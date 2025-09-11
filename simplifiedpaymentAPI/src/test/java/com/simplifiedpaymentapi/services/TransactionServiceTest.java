package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.dtos.TransactionDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.NotAuthorized;
import com.simplifiedpaymentapi.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionValidator transactionValidator;

    @InjectMocks
    private TransactionService transactionService;

    private final Long PAYER_ID = 1L;
    private final Long PAYEE_ID = 2L;
    private final BigDecimal VALUE = new BigDecimal(10);
    private final BigDecimal INITIAL_BALANCE_PAYER = new BigDecimal(100);
    private final BigDecimal INITIAL_BALANCE_PAYEE = new BigDecimal(50);

    @Test
    @DisplayName("Should create transaction successfully when all conditions are met")
    void shouldCreateTransactionSuccessfully() throws Exception {
        User payer = new User(PAYER_ID, "Maria", "12345678910", "123456", "maria@gmail.com", INITIAL_BALANCE_PAYER, UserType.COMMON);
        User payee = new User(PAYEE_ID, "John", "12345678911", "123456", "john@gmail.com", INITIAL_BALANCE_PAYEE, UserType.MERCHANT);

        when(userService.findUserbyId(PAYER_ID)).thenReturn(payer);
        when(userService.findUserbyId(PAYEE_ID)).thenReturn(payee);

        doNothing().when(transactionValidator).validateTransaction(any(User.class), any(BigDecimal.class));
        when(authorizationService.authorizedTransaction()).thenReturn(true);

        TransactionDTO transactionDTO = new TransactionDTO(VALUE, PAYER_ID, PAYEE_ID);
        transactionService.createTransaction(transactionDTO);

        verify(transactionValidator, times(1)).validateTransaction(payer, VALUE);
        verify(authorizationService, times(1)).authorizedTransaction();
        verify(transactionRepository, times(1)).save(any());
        verify(userService, times(1)).saveUser(payer);
        verify(userService, times(1)).saveUser(payee);
        verify(notificationService, times(1)).sendNotification(payer, "Transaction completed successfully");
        verify(notificationService, times(1)).sendNotification(payee, "Transaction received successfully");

        Assertions.assertEquals(INITIAL_BALANCE_PAYER.subtract(VALUE), payer.getBalance());
        Assertions.assertEquals(INITIAL_BALANCE_PAYEE.add(VALUE), payee.getBalance());
    }

    @Test
    @DisplayName("Should throw an exception when authorization fails")
    void shouldThrowExceptionWhenAuthorizationFails() throws Exception {
        User payer = new User(PAYER_ID, "Maria", "12345678910", "123456", "maria@gmail.com", INITIAL_BALANCE_PAYER, UserType.COMMON);
        User payee = new User(PAYEE_ID, "John", "12345678911", "123456", "john@gmail.com", INITIAL_BALANCE_PAYEE, UserType.MERCHANT);

        when(userService.findUserbyId(PAYER_ID)).thenReturn(payer);
        when(userService.findUserbyId(PAYEE_ID)).thenReturn(payee);

        when(authorizationService.authorizedTransaction()).thenReturn(false);

        TransactionDTO transactionDTO = new TransactionDTO(VALUE, PAYER_ID, PAYEE_ID);

        Assertions.assertThrows(NotAuthorized.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        verify(transactionValidator, times(1)).validateTransaction(payer, VALUE);
        verify(authorizationService, times(1)).authorizedTransaction();
        verify(transactionRepository, never()).save(any());
        verify(userService, never()).saveUser(any());
        verify(notificationService, never()).sendNotification(any(), any());
    }

    @Test
    @DisplayName("Should throw Exception NotAuthorized when validation fails")
    void shouldThrowExceptionWhenValidationFails() throws Exception {
        User payer = new User(PAYER_ID, "Maria", "12345678910", "123456", "maria@gmail.com", INITIAL_BALANCE_PAYER, UserType.COMMON);
        User payee = new User(PAYEE_ID, "John", "12345678911", "123456", "john@gmail.com", INITIAL_BALANCE_PAYEE, UserType.MERCHANT);

        when(userService.findUserbyId(PAYER_ID)).thenReturn(payer);
        when(userService.findUserbyId(PAYEE_ID)).thenReturn(payee);

        doThrow(new NotAuthorized("Insufficient balance for the transaction")).when(transactionValidator).validateTransaction(any(User.class), any(BigDecimal.class));

        TransactionDTO transactionDTO = new TransactionDTO(VALUE, PAYER_ID, PAYEE_ID);

        Assertions.assertThrows(NotAuthorized.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        verify(transactionValidator, times(1)).validateTransaction(payer, VALUE);
        verify(authorizationService, never()).authorizedTransaction();
        verify(transactionRepository, never()).save(any());
        verify(userService, never()).saveUser(any());
        verify(notificationService, never()).sendNotification(any(), any());
    }
}