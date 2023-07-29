package com.authentation.service;

import com.authentation.model.User;
import com.authentation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User user = userRepository.findByEmail(username);

      /*  if (username.equals("loki")){
            return new User("loki", "loki", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("user not found");
        }*/
        return user;
    }
}
