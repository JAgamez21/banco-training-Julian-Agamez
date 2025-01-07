package com.example.banco_training_Julian_Agamez.service;

import com.example.banco_training_Julian_Agamez.exception.UsuarioNoEncontradoException;
import com.example.banco_training_Julian_Agamez.model.Usuario;
import com.example.banco_training_Julian_Agamez.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
        return usuarioRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con identificación: " + identificacion));
    }
}
