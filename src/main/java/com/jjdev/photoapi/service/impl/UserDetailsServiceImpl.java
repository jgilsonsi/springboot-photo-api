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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    AccountService accountService;

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
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<UserRole> userRoles = user.getUserRoles();
        userRoles.forEach(userRole -> {
            authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
        });
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}