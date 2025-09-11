package com.simplifiedpaymentapi.exceptions;

public class NotAuthorized extends Exception{
    public NotAuthorized(String message){
        super(message);
    }
}
