package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_successful() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Andrea Sierra");
        user1.setIdentification("123456");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Julian Agamez");
        user2.setIdentification("789012");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        ApiResponse<List<User>> response = userService.getAllUsers();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("OPERACION_EXITOSA", response.getCodigo());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_noContent() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        ApiResponse<List<User>> response = userService.getAllUsers();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("SIN_REGISTROS", response.getCodigo());
        assertNull(response.getData());
    }

    @Test
    void getUserById_successful() {
        User user = new User();
        user.setId(1L);
        user.setName("Andrea Sierra");
        user.setIdentification("123456");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ApiResponse<User> response = userService.getUserById(1L);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("OPERACION_EXITOSA", response.getCodigo());
        assertNotNull(response.getData());
        assertEquals("123456", response.getData().getIdentification());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("Usuario no encontrado con identificación: 1", exception.getMessage());
    }

    @Test
    void createUser_successful() {
        User newUser = new User();
        newUser.setName("Andrea Sierra");
        newUser.setIdentification("123456");

        when(userRepository.findByIdentification("123456")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        ApiResponse<User> response = userService.createUser(newUser);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("USUARIO_CREADO", response.getCodigo());
        assertNotNull(response.getData());
        assertEquals("Andrea Sierra", response.getData().getName());
    }

    @Test
    void createUser_userAlreadyExists() {
        User existingUser = new User();
        existingUser.setName("Andrea Sierra");
        existingUser.setIdentification("123456");

        when(userRepository.findByIdentification("123456")).thenReturn(Optional.of(existingUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(existingUser);
        });

        assertEquals("El usuario con la identificación 123456 ya existe.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
