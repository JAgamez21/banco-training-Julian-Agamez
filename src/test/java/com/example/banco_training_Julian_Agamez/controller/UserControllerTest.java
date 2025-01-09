package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.dto.ApiResponse;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;

@WebMvcTest(controllers = UserController.class)
@Import(UserService.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final UserService userService = mock(UserService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setIdentification("12345");
        user1.setName("Andrea Sierra");

        User user2 = new User();
        user2.setIdentification("67890");
        user2.setName("Carlos López");

        List<User> users = Arrays.asList(user1, user2);
        ApiResponse<List<User>> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Usuarios obtenidos con éxito.", users);

        Mockito.when(userService.getAllUsers()).thenReturn(apiResponse);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Usuarios obtenidos con éxito"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldGetUserByIdentification() throws Exception {
        User user = new User();
        user.setIdentification("12345");
        user.setName("Andrea Sierra");

        ApiResponse<User> apiResponse = new ApiResponse<>(200, "OPERACION_EXITOSA", "Usuario encontrado con éxito.", user);

        Mockito.when(userService.getUserByIdentification("12345")).thenReturn(apiResponse);

        mockMvc.perform(get("/api/users/get_user/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("OPERACION_EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Usuario encontrado con éxito"))
                .andExpect(jsonPath("$.data.identification").value("12345"))
                .andExpect(jsonPath("$.data.name").value("Andrea Sierra"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User();
        user.setIdentification("12345");
        user.setName("Andrea Sierra");

        ApiResponse<User> apiResponse = new ApiResponse<>(201, "USUARIO_CREADO", "Usuario creado con éxito.", user);

        Mockito.when(userService.createUser(any(User.class))).thenReturn(apiResponse);

        mockMvc.perform(post("/api/users/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.codigo").value("USUARIO_CREADO"))
                .andExpect(jsonPath("$.mensaje").value("Usuario creado con éxito"))
                .andExpect(jsonPath("$.data.identification").value("12345"))
                .andExpect(jsonPath("$.data.name").value("Andrea Sierra"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = new User();
        user.setIdentification("12345");
        user.setName("Andrea Sierra Updated");

        ApiResponse<User> apiResponse = new ApiResponse<>(200, "USUARIO_ACTUALIZADO", "Usuario actualizado con éxito.", user);

        Mockito.when(userService.updateUser(eq("12345"), any(User.class))).thenReturn(apiResponse);

        mockMvc.perform(put("/api/users/update_user/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.codigo").value("USUARIO_ACTUALIZADO"))
                .andExpect(jsonPath("$.mensaje").value("Usuario actualizado con éxito"))
                .andExpect(jsonPath("$.data.name").value("Andrea Sierra Updated"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.getUserByIdentification("99999"))
                .thenThrow(new RuntimeException("Usuario no encontrado con identificación: 99999"));

        mockMvc.perform(get("/api/users/get_user/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("ERROR_INTERNO"))
                .andExpect(jsonPath("$.mensaje").value("Usuario no encontrado con identificación: 99999"));
    }
}
