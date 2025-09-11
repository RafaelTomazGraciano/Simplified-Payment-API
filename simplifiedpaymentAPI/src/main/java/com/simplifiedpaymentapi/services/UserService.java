package com.simplifiedpaymentapi.services;

import com.simplifiedpaymentapi.dtos.UserDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.exceptions.UserValidationException;
import com.simplifiedpaymentapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidatorService userValidatorService;

    public User findUserbyId(Long id){
        return this.userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found"));
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public User createUser(UserDTO userDTO){
        User newUser = new User(userDTO);
        userValidatorService.validateUser(newUser);
        this.userRepository.save(newUser);
        return newUser;
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }

}
