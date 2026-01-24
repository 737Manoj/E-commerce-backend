package com.Manoj.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                 String jwt = null;
                 final String username;

                 if(request.getCookies() != null) {
                     for(Cookie cookie : request.getCookies()) {
                         if("jwt".equals(cookie.getName())) {
                             jwt = cookie.getValue();
                             break;
                         }
                     }
                 }
                 if(jwt == null) {
                     filterChain.doFilter(request, response);
                     return;
                 }

                 username = jwtService.extractUsername(jwt);

                 if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                     UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                     if(jwtService.isTokenValid(jwt, userDetails)) {
                         UsernamePasswordAuthenticationToken authToken = new  UsernamePasswordAuthenticationToken(
                                 userDetails,
                                 null,
                                 userDetails.getAuthorities()
                         );
                         authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                         SecurityContextHolder.getContext().setAuthentication(authToken);
                     }
                 }
                 filterChain.doFilter(request, response);
    }
}
