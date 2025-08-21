package com.simplifiedpicpay.services;

import com.simplifiedpicpay.dtos.TransactionDTO;
import com.simplifiedpicpay.entities.Transaction;
import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.exceptions.NotAuthorized;
import com.simplifiedpicpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Transaction creteTransaction(TransactionDTO transactionDTO) throws Exception{
        User payer = this.userService.findUserbyId(transactionDTO.payerId());
        User payee = this.userService.findUserbyId(transactionDTO.payeeId());

        userService.validateTransaction(payer, transactionDTO.value());

        if(!authorizedTransaction(payer, transactionDTO.value())){
            throw new NotAuthorized("Transaction not authorized");
        }

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

        notificationService.sendNotification(payer, "Transaction completed successfully");
        notificationService.sendNotification(payee, "Transaction received successfully");

        return newTransaction;
    }

    public boolean authorizedTransaction(User payer, BigDecimal value) throws Exception{
        String url = "https://util.devi.tools/api/v2/authorize";

        try{
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        }catch(HttpClientErrorException | HttpServerErrorException e){
            return false;
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

}
