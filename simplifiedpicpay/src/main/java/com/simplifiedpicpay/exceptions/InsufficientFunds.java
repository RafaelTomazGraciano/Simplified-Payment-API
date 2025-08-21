package com.simplifiedpicpay.exceptions;

public class InsufficientFunds extends Exception{
    public InsufficientFunds(String message){
        super(message);
    }
}
