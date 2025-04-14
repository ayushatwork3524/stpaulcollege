package com.main.stpaul.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.main.stpaul.constants.Role;
import com.main.stpaul.jwtSecurity.CustomerUserDetail;
import com.main.stpaul.jwtSecurity.JwtValidator;
import com.main.stpaul.services.impl.SessionServiceImpl;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private CustomerUserDetail customerUserDetail;

    @Autowired
    private SessionServiceImpl sessionServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
            .sessionManagement(managment->managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize->authorize
                .requestMatchers("api/auth/**","swagger-ui/**","/v3/**").permitAll()
                .requestMatchers("api/user/**").hasAnyRole(Role.MANAGER.toString(),Role.ADMIN.toString(),Role.SUPERADMIN.toString(),Role.ACOUNTANT.toString())
                .requestMatchers("api/manager/**").hasAnyRole(Role.MANAGER.toString(),Role.ADMIN.toString(),Role.SUPERADMIN.toString())
                .requestMatchers("api/accountant/**").hasAnyRole(Role.ACOUNTANT.toString(),Role.SUPERADMIN.toString())
                .requestMatchers("api/admin/**").hasAnyRole(Role.ADMIN.toString(),Role.SUPERADMIN.toString())
                .requestMatchers("api/superadmin/**").hasAnyRole(Role.SUPERADMIN.toString())
                .anyRequest().authenticated())
            .addFilterBefore(new JwtValidator(sessionServiceImpl), UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf->csrf.disable())
            .cors(cors->cors.configurationSource(corsConfigurationSource()))
            .authenticationProvider(authenticationProvider());

            return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.customerUserDetail);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        return request->{
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
            configuration.setAllowedMethods(Arrays.asList("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);
            configuration.setExposedHeaders(Arrays.asList("Authorization"));
            configuration.setMaxAge(3600L);
            return configuration;
        };
    }

     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}