package com.simplifiedpaymentapi.controllers;

import com.simplifiedpaymentapi.dtos.TransactionDTO;
import com.simplifiedpaymentapi.entities.Transaction;
import com.simplifiedpaymentapi.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
@Tag(name = "transactions", description = "Endpoints for transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Executes a transaction", description = "Executes a transaction")
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception{
        Transaction newTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }

}
