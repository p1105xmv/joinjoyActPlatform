package com.joinjoy.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.joinjoy.model.bean.Userinfo;

public class MyUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    @Autowired
    private Userinfo userinfo;

    public MyUser(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userinfo.getWebRole().getWrid().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userinfo.getUPwd();
    }

    @Override
    public String getUsername() {
        return userinfo.getUAccountEmail();
    }

    public Integer getUserid() {
        return userinfo.getUserid();
    }

    public String getImgpath() {
        return userinfo.getUImgpath();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    
}