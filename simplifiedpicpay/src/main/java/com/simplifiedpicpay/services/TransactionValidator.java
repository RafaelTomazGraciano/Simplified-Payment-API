package com.simplifiedpicpay.services;

import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.entities.UserType;
import com.simplifiedpicpay.exceptions.InsufficientFunds;
import com.simplifiedpicpay.exceptions.NotAuthorized;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
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
