package com.main.stpaul.jwtSecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class JwtConstants {
    
    public static String JWT_HEADER = "Authorization";

    
    @Value("${secret.key}")
    private String apiKey;

    public static String SECRETE_KEY ;

    @PostConstruct
    public void init(){
        SECRETE_KEY = apiKey;
    }

    public static String getApiKey(){
        return SECRETE_KEY;
    }
}
