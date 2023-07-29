package com.authentation.controller;

import com.authentation.helper.JwtUtil;
import com.authentation.model.JwtRequest;
import com.authentation.model.JwtResponse;
import com.authentation.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){
        System.out.println(jwtRequest);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        } catch (UsernameNotFoundException e){
            e.printStackTrace();
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } catch (BadCredentialsException e){
            e.printStackTrace();
            throw new BadCredentialsException("BadCredentialsException");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        System.out.println("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
