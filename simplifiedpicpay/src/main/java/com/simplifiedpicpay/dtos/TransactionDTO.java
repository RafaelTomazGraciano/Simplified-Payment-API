package com.simplifiedpicpay.dtos;


import java.math.BigDecimal;

public record TransactionDTO (BigDecimal value, Long payerId, Long payeeId){
}
