package com.simplifiedpicpay.services;

import com.simplifiedpicpay.dtos.UserDTO;
import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.entities.UserType;
import com.simplifiedpicpay.exceptions.InsufficientFunds;
import com.simplifiedpicpay.exceptions.NotAuthorized;
import com.simplifiedpicpay.exceptions.UserValidationException;
import com.simplifiedpicpay.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User payer, BigDecimal value) throws Exception{
        if(payer.getUserType() == UserType.MERCHANT){
            throw new NotAuthorized("Merchant user is not authorized to accomplish a transaction");
        }

        if (payer.getBalance().compareTo(value) < 0) {
            throw new InsufficientFunds("Insufficient funds to accomplish a transaction");
        }
    }

    public User findUserbyId(Long id) throws Exception{
        return this.userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found"));
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

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

    public User creteUser(UserDTO userDTO){
        User newUser = new User(userDTO);
        validateUser(newUser);
        this.userRepository.save(newUser);
        return newUser;
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }

}
