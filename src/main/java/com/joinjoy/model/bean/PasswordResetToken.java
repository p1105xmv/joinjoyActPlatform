package com.joinjoy.model.bean;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PasswordResetToken {
    private static final Integer EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pwdTokenid;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private Userinfo userinfo;

    private Date expiryDate;


    public PasswordResetToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public PasswordResetToken(Userinfo userinfo, String token) {
        this.userinfo = userinfo;
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    // 計算過期時間
    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        // 建立一個 Calendar 物件
        final Calendar cal = Calendar.getInstance();
        // 取得時間
        cal.setTimeInMillis(new Date().getTime());
        // Calendar.MINUTE 是用 expiryTimeInMinutes(int) 指定的分鐘數
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());

    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

}
