package org.com.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.com.model.User;
import org.com.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service 
public class CustomUserDetails implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetails(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = (user.getRoles() == null)
                ? Collections.emptyList()
                : user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                authorities);
    }

}
