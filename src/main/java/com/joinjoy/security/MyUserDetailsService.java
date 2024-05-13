package com.joinjoy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public MyUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Userinfo userinfo = userService.findUserByuAccountEmail(username);
        if (userinfo == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new MyUser(userinfo);

    }

}
