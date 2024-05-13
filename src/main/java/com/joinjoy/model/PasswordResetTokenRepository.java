package com.joinjoy.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.PasswordResetToken;
import com.joinjoy.model.bean.Userinfo;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserinfo(Userinfo useruinfo);

}
