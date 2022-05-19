package com.sojicute.petprojectdemo.security.jwt;

import com.sojicute.petprojectdemo.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class JwtProvider {

    private String jwtSecret = "secret";

    public String generateAccessToken(User user) {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token" + e.getMessage());
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).getBody();
        return claims.getSubject();
    }
}
