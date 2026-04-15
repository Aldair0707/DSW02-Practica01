package com.dsw02.empleado.infrastructure.security;

import com.dsw02.empleado.api.dto.ErrorResponse;
import com.dsw02.empleado.domain.CorreoNormalizer;
import com.dsw02.empleado.domain.ErrorCode;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final EmpleadoRepository empleadoRepository;
    private final CorreoNormalizer correoNormalizer;

    public SecurityConfig(
        ObjectMapper objectMapper,
        EmpleadoRepository empleadoRepository,
        CorreoNormalizer correoNormalizer
    ) {
        this.objectMapper = objectMapper;
        this.empleadoRepository = empleadoRepository;
        this.correoNormalizer = correoNormalizer;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/empleados/auth/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/empleados").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers(HttpMethod.GET, "/api/v1/departamentos").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers("/api/v1/empleados/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/departamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/empleados/*/estado").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/empleados/*/departamento").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.AUTH_INVALIDA.name(), "Credenciales invalidas")
                )
                .accessDeniedHandler((request, response, accessDeniedException) ->
                    writeError(response, HttpServletResponse.SC_FORBIDDEN, ErrorCode.NO_AUTORIZADO.name(), "No autorizado")
                )
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    UserDetailsService userDetailsService(
        @Value("${APP_BASIC_USER:admin}") String username,
        @Value("${APP_BASIC_PASSWORD:admin123}") String password,
        PasswordEncoder passwordEncoder
    ) {
        return userInput -> {
            if (username.equals(userInput)) {
                return User.withUsername(username)
                    .password(passwordEncoder.encode(password))
                    .roles("ADMIN")
                    .build();
            }

            String correo = correoNormalizer.normalize(userInput);
            EmpleadoEntity empleado = empleadoRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales invalidas"));

            return User.withUsername(empleado.getCorreo())
                .password(empleado.getPasswordHash())
                .roles("EMPLEADO")
                .disabled(!empleado.isActivo())
                .build();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void writeError(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(code, message, List.of()));
    }
}
