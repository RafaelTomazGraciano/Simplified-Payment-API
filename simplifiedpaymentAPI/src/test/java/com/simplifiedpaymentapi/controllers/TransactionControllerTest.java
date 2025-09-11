package com.simplifiedpaymentapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifiedpaymentapi.dtos.TransactionDTO;
import com.simplifiedpaymentapi.entities.Transaction;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.InsufficientFunds;
import com.simplifiedpaymentapi.services.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TransactionService transactionService;

    @Test
    @DisplayName("Should return a new transaction with a 200 OK status")
    void shouldReturnNewTransaction() throws Exception {
        Transaction newTransaction = new Transaction();
        final Long PAYER_ID = 1L;
        final Long PAYEE_ID = 2L;
        final BigDecimal VALUE = new BigDecimal(10);

        User payer = new User(PAYER_ID, "Maria", "1234", "123", "maria@gmail.com", new BigDecimal(100), UserType.COMMON);
        User payee = new User(PAYEE_ID, "John", "5678", "456", "john@gmail.com", new BigDecimal(50), UserType.MERCHANT);

        newTransaction.setId(1L);
        newTransaction.setValue(VALUE);
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);

        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(newTransaction);

        TransactionDTO transactionDTO = new TransactionDTO(VALUE, PAYER_ID, PAYEE_ID);

        mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transactionDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(newTransaction.getId()))
            .andExpect(jsonPath("$.value").value(newTransaction.getValue()))
            .andExpect(jsonPath("$.payer.id").value(newTransaction.getPayer().getId()))
            .andExpect(jsonPath("$.payee.id").value(newTransaction.getPayee().getId()));
    }

    @Test
    @DisplayName("Should return an Exception when Fund is Insufficient")
    void shouldReturnForbiddenWhenTransactionServiceThrowsException() throws Exception {
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(new InsufficientFunds("Insufficient fund"));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(10), 1L, 2L);

        mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transactionDTO)))
            .andExpect(status().isForbidden())
            .andExpect(content().string(containsString("Insufficient fund")));
    }
}