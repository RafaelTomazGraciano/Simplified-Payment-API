package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.exceptions.UserValidationException;
import com.simplifiedpaymentapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserValidatorService {

    private final UserRepository userRepository;

    public void validateUser(User user){
        Map<String, String> errors = new HashMap<>();
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", "Email already exists");
        }
        if (userRepository.existsByDocument(user.getDocument())){
            errors.put("document", "Document already exists");
        }
        if(!errors.isEmpty()){
            throw new UserValidationException(errors);
        }
    }
}
