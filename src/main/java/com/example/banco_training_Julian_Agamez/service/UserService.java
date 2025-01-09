package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "SIN_REGISTROS", "No se encontraron usuarios registrados.", null);
        }
        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Usuarios obtenidos con éxito.", users);
    }

    public ApiResponse<User> getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con identificación: " + id));

        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Usuario encontrado con éxito.", user);
    }

    public ApiResponse<User> getUserByIdentification(String identification) {
        if (identification == null || identification.isEmpty()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía.");
        }

        User user = userRepository.findByIdentification(identification)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con identificación: " + identification));

        return new ApiResponse<>(HttpStatus.OK.value(), "OPERACION_EXITOSA", "Usuario encontrado con éxito.", user);
    }

    public ApiResponse<User> createUser(User newUser) {
        if (newUser.getIdentification() == null || newUser.getIdentification().isEmpty()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía.");
        }
        if (userRepository.findByIdentification(newUser.getIdentification()).isPresent()) {
            throw new IllegalArgumentException("El usuario con la identificación " + newUser.getIdentification() + " ya existe.");
        }

        User createdUser = userRepository.save(newUser);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "USUARIO_CREADO", "Usuario creado con éxito.", createdUser);
    }

    public ApiResponse<User> updateUser(String identification, User updatedUser) {
        User existingUser = userRepository.findByIdentification(identification)
                .orElseThrow(() -> new UserNotFoundException("Usuario con identificación " + identification + " no encontrado."));

        existingUser.setName(updatedUser.getName());
        existingUser.setIdentification(updatedUser.getIdentification());

        User savedUser = userRepository.save(existingUser);
        return new ApiResponse<>(HttpStatus.OK.value(), "USUARIO_ACTUALIZADO", "Usuario actualizado con éxito.", savedUser);
    }
}
