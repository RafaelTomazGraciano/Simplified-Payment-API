package com.simplifiedpaymentapi.controllers;

import com.simplifiedpaymentapi.dtos.UserDTO;
import com.simplifiedpaymentapi.entities.User;
import com.simplifiedpaymentapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Creates a new user", description = "Register a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Operation(summary = "List all users", description = "Retrieves all registered users")
    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = this.userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
