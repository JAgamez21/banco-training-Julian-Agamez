package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get_user/{identification}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String identification) {
        ApiResponse<User> response = userService.getUserByIdentification(identification);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create_user")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User newUser) {
        try {
            if (newUser.getIdentification() == null || newUser.getIdentification().isEmpty()) {
                throw new IllegalArgumentException("The identification cannot be empty.");
            }
            ApiResponse<User> response = userService.createUser(newUser);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "INVALID_ARGUMENT", ex.getMessage(), null)
            );
        }
    }

    @PutMapping("/update_user/{identification}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String identification, @RequestBody User updatedUser) {
        ApiResponse<User> response = userService.updateUser(identification, updatedUser);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
