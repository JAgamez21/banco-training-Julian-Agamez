package com.example.banco_training_Julian_Agamez.repository;

import com.example.banco_training_Julian_Agamez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByIdentificacion(String identificacion);
}
