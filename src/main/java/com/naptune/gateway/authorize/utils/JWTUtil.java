package com.naptune.gateway.authorize.utils;

import com.naptune.gateway.authorize.details.AdminDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    public static final String TOKEN_ID = "token_id";
    public static final String ROLE_ID = "role";

    @Value("${neptune.oauth.jjwt.secret}")
    private String secret;

    @Value("${neptune.oauth.jjwt.expiration.access}")
    private String expirationTime;

    @Value("${neptune.oauth.jjwt.expiration.refresh}")
    private String expirationRefreshTime;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(Claims claims) {
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(Claims claims) {
        return claims.getExpiration();
    }

    private Boolean isTokenExpired(Claims claims) {
        final Date expiration = getExpirationDateFromToken(claims);
        return expiration.before(new Date());
    }

//    public String generateToken(UserDetails userDetails) {
//        AdminDetails adminDetails = (AdminDetails)userDetails;
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", adminDetails.getRoleList().get(0).getRoleName());
//        return doGenerateToken(claims, adminDetails.getUsername());
//    }

    public String generateToken(UserDetails userDetails,TokenType tokenType,String tokenId) {
        AdminDetails adminDetails = (AdminDetails)userDetails;
        Map<String, Object> claims = new HashMap<>();
//        claims.put("token_type",tokenType.name());
        claims.put(TOKEN_ID,tokenId);
        claims.put(ROLE_ID, adminDetails.getRoleList().get(0).getRoleName());
        return doGenerateToken(claims, adminDetails.getUsername(),tokenType);
    }

    private String doGenerateToken(Map<String, Object> claims, String username,TokenType tokenType) {
        String expireTime = tokenType == TokenType.ACCESS ? expirationTime: expirationRefreshTime;
        long expirationTimeLong = Long.parseLong(expireTime); //in second

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .setIssuer(tokenType.name())
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(getAllClaimsFromToken(token));
    }

    public boolean validateAccessToken(Claims claims){
        return TokenType.ACCESS.name().equals(claims.getIssuer());
    }

    public boolean validateRefreshToken(Claims claims){
        return TokenType.REFRESH.name().equals(claims.getIssuer());
    }

    public String createAccessTokenByRefreshToken(String refreshToken){
        Claims claims = getAllClaimsFromToken(refreshToken);
        if(validateRefreshToken(claims) && validateToken(refreshToken)){
            long expirationTimeLong = Long.parseLong(expirationTime); //in second
            final Date createdDate = new Date();
            final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

            String username = claims.getSubject();
            Map<String, Object> newClaims = new HashMap<>();
            claims.forEach(newClaims::put);

            return Jwts.builder()
                    .setClaims(newClaims)
                    .setSubject(username)
                    .setIssuedAt(createdDate)
                    .setExpiration(expirationDate)
                    .setIssuer(TokenType.ACCESS.name())
                    .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                    .compact();
        }
        return null;
    }

}
