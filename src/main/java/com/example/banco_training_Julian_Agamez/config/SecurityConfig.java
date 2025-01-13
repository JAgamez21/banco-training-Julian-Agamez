package com.example.banco_training_Julian_Agamez.config;

import com.example.banco_training_Julian_Agamez.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login").permitAll() // Acceso público a estas rutas
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // Deshabilitar protección de frames (para H2 Console)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Añadir el filtro JWT antes del filtro de autenticación
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Página de inicio de sesión personalizada
                        .usernameParameter("identification") // Nombre del parámetro para el nombre de usuario
                        .passwordParameter("password") // Nombre del parámetro para la contraseña
                        .defaultSuccessUrl("/api/users/get_user/{username}", true) // Redirección al inicio tras éxito
                        .failureUrl("/login?error=true") // Redirección en caso de error
                        .permitAll() // Permitir acceso a la página de inicio de sesión
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL para cerrar sesión
                        .logoutSuccessUrl("/login?logout=true") // Redirección tras cierre de sesión
                        .permitAll() // Permitir acceso a la URL de cierre de sesión
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
