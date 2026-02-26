package com.nhiennhatt.bookstoreapi.common.filters;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.CustomUserDetails;
import com.nhiennhatt.bookstoreapi.services.CustomUserDetailsService;
import com.nhiennhatt.bookstoreapi.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil accessTokenService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    public void setAccessTokenService(@Lazy JwtUtil accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = accessTokenService.verify(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUser(), null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
