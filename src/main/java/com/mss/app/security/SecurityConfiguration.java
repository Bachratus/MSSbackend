package com.mss.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class SecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfiguration(AuthenticationConfiguration authenticationConfiguration,
            CustomUserDetailsService customUserDetailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(Arrays.asList("http://localhost:9000"));
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                            config.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                            config.setExposedHeaders(Arrays.asList("x-auth-token"));
                            config.setAllowCredentials(true);
                            config.setMaxAge(3600L);
                            return config;
                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/authenticate").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/authority/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USERS_ADMINISTRATION)

                        .requestMatchers("/api/users/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USERS_ADMINISTRATION)

                        .requestMatchers("api/tasks/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.WEEKLY_REPORTS)

                        .requestMatchers(HttpMethod.GET, "api/task-reports/weekly/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.WEEKLY_REPORTS)

                        .requestMatchers(HttpMethod.POST, "api/task-reports/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.WEEKLY_REPORTS)

                        .requestMatchers(HttpMethod.PUT, "api/task-reports/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.WEEKLY_REPORTS)

                        .requestMatchers(HttpMethod.DELETE, "api/task-reports/**")
                        .hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.WEEKLY_REPORTS)

                        .anyRequest().authenticated())
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
