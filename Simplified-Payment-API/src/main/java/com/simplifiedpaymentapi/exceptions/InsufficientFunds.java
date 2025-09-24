package com.simplifiedpaymentapi.exceptions;

public class InsufficientFunds extends Exception{
    public InsufficientFunds(String message){
        super(message);
    }
}
