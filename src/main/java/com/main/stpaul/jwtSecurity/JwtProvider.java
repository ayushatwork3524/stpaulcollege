package com.main.stpaul.jwtSecurity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;

import com.main.stpaul.entities.Session;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtProvider {

    public static SecretKey key = Keys.hmacShaKeyFor(JwtConstants.getApiKey().getBytes());
    
    @SuppressWarnings("deprecation")
    public static Session generateJwtToken(Authentication auth){
        Session session = new Session();
            String jwt = Jwts.builder()
                        .setIssuer("ok").setIssuedAt(new Date())
                        .setExpiration(new Date(new Date().getTime()+24*60*60*1000))
                        .claim("email", auth.getName())
                        .claim("role", auth.getAuthorities().toArray()[0].toString())
                        .signWith(key)
                        .compact();

        session.setIssueAt(LocalDateTime.now());
        session.setToken(jwt);
        session.setExpiration(24*60*60*1000l);
        return session;
    }

    @SuppressWarnings("deprecation")
    public static String getEmailFromJwt(String jwt){
        jwt = jwt.substring(7);

        Claims claims = Jwts.parser()
                    .setSigningKey(key).build()
                    .parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    @SuppressWarnings("deprecation")
    public static String getRoleFromJwt(String jwt){
        jwt = jwt.substring(7);
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(key).build()
                    .parseClaimsJws(jwt).getBody();
                        
        String role = claims.get("role").toString();
        return role;
        }catch(Exception e){
            e.printStackTrace();
            return "not ok";
        }
    }
}
