package com.simplifiedpaymentapi.dtos;

import com.simplifiedpaymentapi.entities.UserType;
import lombok.Getter;

import java.math.BigDecimal;

public record UserDTO(String name, String password, String document, String email, BigDecimal balance, UserType userType) {
}
