package com.jjdev.photoapi.service.impl;

import com.jjdev.photoapi.model.UserRole;
import com.jjdev.photoapi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountService accountService;

    @Autowired
    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.jjdev.photoapi.model.User user = accountService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " was not found");
        }
        Collection<GrantedAuthority> authorities;
        Set<UserRole> userRoles = user.getUserRoles();
        authorities = userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRoles.toString()))
                .collect(Collectors.toList());
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}