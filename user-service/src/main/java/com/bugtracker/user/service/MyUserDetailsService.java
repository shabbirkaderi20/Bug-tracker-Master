package com.bugtracker.user.service;

import com.bugtracker.user.entity.User;
import com.bugtracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(userName).get();

        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),getAuthority(user));
    }
    private Set getAuthority(User user) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + "admin"));

        authorities.add(new SimpleGrantedAuthority("ROLE_" + "staff"));

        authorities.add(new SimpleGrantedAuthority("ROLE_" + "client"));



        return authorities;

    }
}
