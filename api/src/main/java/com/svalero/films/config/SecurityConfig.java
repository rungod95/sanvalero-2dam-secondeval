package com.svalero.films.config;

import com.svalero.films.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Profile({"!test"})
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a login y registro
                        .requestMatchers("api/auth/login", "api/auth/register").permitAll()

                        // Permitir GET, POST y PUT en films y directors sin autenticación
                        .requestMatchers(HttpMethod.GET, "/films", "/films/{id}", "/directors", "/directors/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/films", "/directors").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/films/{id}", "/directors/{id}").permitAll()

                        // Requiere autenticación solo para DELETE en films y directors
                        .requestMatchers(HttpMethod.DELETE, "/films/{id}", "/directors/{id}").authenticated()

                        // Cualquier otra petición también permitida (puedes cambiar esto si es necesario)
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
