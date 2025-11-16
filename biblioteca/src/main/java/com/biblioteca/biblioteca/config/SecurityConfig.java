package com.biblioteca.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    /**
     * Define o "Bean" que ensina o Spring a criar um
     * PasswordEncoder (usando o algoritmo BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração global de CORS (para corrigir o 403)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Regras de segurança (para corrigir o 403)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Aplica a configuração de CORS global
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Desabilita o CSRF
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configura as regras de autorização
            .authorizeHttpRequests(auth -> auth
                // Permite acesso PÚBLICO a /api/ E TUDO DENTRO
                .requestMatchers("/api/**").permitAll() 
                
                // Pede autenticação para qualquer outra URL
                .anyRequest().authenticated() 
            );
            
        return http.build();
    }
}