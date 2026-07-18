package ru.xromza.user.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authenticationProvider;
        private final HandlerExceptionResolver resolver;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationProvider authenticationProvider,
                        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.authenticationProvider = authenticationProvider;
                this.resolver = resolver;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(request -> {
                                        var corsConfiguration = new CorsConfiguration();
                                        corsConfiguration.setAllowedOriginPatterns(
                                                        List.of("http://localhost:3000", "http://192.168.*.*:3000",
                                                                        "https://dev.hekr.tech", "https://hekr.tech"));
                                        corsConfiguration.setAllowedMethods(
                                                        List.of("GET", "POST", "DELETE", "OPTIONS", "PUT", "PATCH"));
                                        corsConfiguration.setAllowedHeaders(List.of("*"));
                                        corsConfiguration.setAllowCredentials(true);
                                        return corsConfiguration;
                                }))
                                .csrf(AbstractHttpConfigurer::disable)
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint((request, response, authException) -> resolver
                                                                .resolveException(request, response, null,
                                                                                authException))
                                                .accessDeniedHandler((request, response,
                                                                accessDeniedException) -> resolver.resolveException(
                                                                                request, response, null,
                                                                                accessDeniedException)))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/swagger-ui/**").permitAll()
                                                .requestMatchers("/v3/api-docs/**").permitAll()
                                                .requestMatchers("/api/v1/user/auth").permitAll()
                                                .requestMatchers("/api/v1/user/auth/**").permitAll()
                                                .requestMatchers("/api/v1/internal/user/name").permitAll()
                                                .requestMatchers("/api/v1/user/profile")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER")
                                                .requestMatchers("/api/v1/user/profile/*")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER")
                                                .requestMatchers(HttpMethod.POST, "/api/v1/user/profile/password")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER")
                                                .requestMatchers(HttpMethod.PATCH, "/api/v1/user/profile")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER")
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
