package com.simplifiedpicpay.controllers;

import com.simplifiedpicpay.dtos.UserDTO;
import com.simplifiedpicpay.entities.User;
import com.simplifiedpicpay.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Creates a new user", description = "Creates a new user in the system")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User newUser = userService.creteUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Operation(summary = "List all users", description = "List all users")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = this.userService.getAllUsers();
        return new  ResponseEntity<>(users, HttpStatus.OK);
    }



}
