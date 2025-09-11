package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.dtos.TransactionDTO;
import com.simplifiedpaymentapi.entities.Transaction;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.exceptions.NotAuthorized;
import com.simplifiedpaymentapi.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final UserService userService;

    private final TransactionRepository transactionRepository;

    private final AuthorizationService authorizationService;

    private final TransactionValidator transactionValidator;

    private final NotificationService notificationService;

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception{
        User payer = this.userService.findUserbyId(transactionDTO.payerId());
        User payee = this.userService.findUserbyId(transactionDTO.payeeId());
        validate(payer, transactionDTO);
        Transaction newTransaction = executeTransaction(payer, payee, transactionDTO);
        notifyUsers(payer, payee);
        return newTransaction;
    }

    private void validate(User payer, TransactionDTO transactionDTO) throws Exception {
        transactionValidator.validateTransaction(payer, transactionDTO.value());

        if(!authorizationService.authorizedTransaction()){
            throw new NotAuthorized("Transaction not authorized");
        }
    }

    private Transaction executeTransaction(User payer, User payee, TransactionDTO transactionDTO){
        Transaction newTransaction = new Transaction();
        newTransaction.setValue(transactionDTO.value());
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setTimestamp(LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(transactionDTO.value()));
        payee.setBalance(payee.getBalance().add(transactionDTO.value()));

        transactionRepository.save(newTransaction);
        userService.saveUser(payer);
        userService.saveUser(payee);

        return newTransaction;
    }

    private void notifyUsers(User payer, User payee){
        notificationService.sendNotification(payer, "Transaction completed successfully");
        notificationService.sendNotification(payee, "Transaction received successfully");
    }

}
