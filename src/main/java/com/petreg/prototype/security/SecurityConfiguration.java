package com.petreg.prototype.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private RoleValidationFilter roleValidationFilter;

    public SecurityConfiguration(RoleValidationFilter roleValidationFilter) {
        this.roleValidationFilter = roleValidationFilter;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Stateless API, no CSRF needed
            .csrf(AbstractHttpConfigurer::disable)
            // Ditto! So no cookies, please
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Stop Spring Security re-triggering authentication on /error
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                // Open public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Secure everything else
                .anyRequest().authenticated()
            )
            // Enable Basic Auth
            .httpBasic(Customizer.withDefaults())
            // Validate active role
            // Important! Must come AFTER BasicAuthenticationFilter has populated
            // the SecurityContext, but BEFORE the request hits controllers
            .addFilterAfter(roleValidationFilter,
                            BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
