package com.simplifiedpicpay.controllers;

import com.simplifiedpicpay.dtos.TransactionDTO;
import com.simplifiedpicpay.entities.Transaction;
import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "transactions", description = "Endpoints for transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Executes a transaction", description = "Executes a transaction")
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception{
        Transaction newTransaction = transactionService.creteTransaction(transactionDTO);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);

    }

    @Operation(summary = "List transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions = this.transactionService.getAllTransactions();
        return new  ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
