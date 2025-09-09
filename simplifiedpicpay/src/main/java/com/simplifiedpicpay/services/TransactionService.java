package com.simplifiedpicpay.services;

import com.simplifiedpicpay.dtos.TransactionDTO;
import com.simplifiedpicpay.entities.Transaction;
import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.exceptions.NotAuthorized;
import com.simplifiedpicpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private UserService userService;

    private TransactionRepository transactionRepository;

    private AuthorizationService authorizationService;

    private TransactionValidator transactionValidator;

    private NotificationService notificationService;

    @Transactional
    public Transaction creteTransaction(TransactionDTO transactionDTO) throws Exception{
        User payer = this.userService.findUserbyId(transactionDTO.payerId());
        User payee = this.userService.findUserbyId(transactionDTO.payeeId());
        validate(payer, transactionDTO);
        Transaction newTransaction = executeTransaction(payer, payee, transactionDTO);
        notifyUsers(payer, payee);
        return newTransaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
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

        return  newTransaction;
    }

    private void notifyUsers(User payer, User payee){
        notificationService.sendNotification(payer, "Transaction completed successfully");
        notificationService.sendNotification(payee, "Transaction received successfully");
    }

}
