package com.example.banco_training_Julian_Agamez.controller;

import com.example.banco_training_Julian_Agamez.model.Usuario;
import com.example.banco_training_Julian_Agamez.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{identificacion}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String identificacion) {
        Usuario usuario = usuarioService.obtenerUsuarioPorIdentificacion(identificacion);
        return ResponseEntity.ok(usuario);
    }
}
