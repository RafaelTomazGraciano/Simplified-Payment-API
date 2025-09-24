package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.entities.UserType;
import com.simplifiedpaymentapi.exceptions.InsufficientFunds;
import com.simplifiedpaymentapi.exceptions.NotAuthorized;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionValidator {

    public void validateTransaction(User payer, BigDecimal value) throws Exception{
        if(payer.getUserType() == UserType.MERCHANT){
            throw new NotAuthorized("Merchant user is not authorized to accomplish a transaction");
        }

        if (payer.getBalance().compareTo(value) < 0) {
            throw new InsufficientFunds("Insufficient funds to accomplish a transaction");
        }
    }

}
