package com.example.banco_training_Julian_Agamez.service;
import com.example.banco_training_Julian_Agamez.exception.UserNotFoundException;
import com.example.banco_training_Julian_Agamez.model.User;
import com.example.banco_training_Julian_Agamez.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        User user = userRepository.findByIdentification(identification).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con identificación: " + identification));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getIdentification())
                .password(user.getPassword())
                .authorities((GrantedAuthority) Collections.singletonList(user.getRole()))
                .build();
    }
}
