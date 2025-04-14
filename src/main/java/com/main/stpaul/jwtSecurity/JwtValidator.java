package com.main.stpaul.jwtSecurity;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.main.stpaul.entities.Session;
import com.main.stpaul.services.impl.SessionServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidator extends OncePerRequestFilter{

    private final SessionServiceImpl sessionServiceImpl;

    public JwtValidator(SessionServiceImpl sessionServiceImpl){
        this.sessionServiceImpl=sessionServiceImpl;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        String jwt = request.getHeader(JwtConstants.JWT_HEADER);
        if(jwt!=null){
                Session session = this.sessionServiceImpl.getSessionByToken(jwt.substring(7));
                if(session==null || !session.isActive() || session.isDelete()){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token has expired, please login again\"}");
                    return;
                }

            try{
                String email = JwtProvider.getEmailFromJwt(jwt);
                String role = JwtProvider.getRoleFromJwt(jwt);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null,List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Token has expired, please login again\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
