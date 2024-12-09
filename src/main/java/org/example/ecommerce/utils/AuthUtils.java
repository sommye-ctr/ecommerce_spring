package org.example.ecommerce.utils;

import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.User;
import org.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    private final UserRepository userRepository;

    @Autowired
    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loggedInUser() {
       Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
       return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User email", Long.parseLong(authentication.getName())));
    }
}
