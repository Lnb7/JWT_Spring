package com.authentation.config;

import com.authentation.helper.JwtUtil;
import com.authentation.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken;

        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                System.out.println("Unable to get Jwt token");
            } catch (ExpiredJwtException e){
                e.printStackTrace();
                System.out.println("Jwt token has expired");
            } catch (MalformedJwtException e){
                e.printStackTrace();
                System.out.println("invalid Jwt");
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                if (jwtUtil.validateToken(jwtToken,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    System.out.println("Jwt token is not valid");
                }
            }else {
                System.out.println("Jwt token not start with Bearer");
            }
        }
        filterChain.doFilter(request,response);
    }
}
