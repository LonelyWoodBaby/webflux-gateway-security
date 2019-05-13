package com.dpacu.gateway.authorize.utils;

import com.dpacu.gateway.authorize.details.AdminDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
//public class JWTUtil {
//
//    @Value("${dpauc.oauth.jjwt.secret}")
//    private String secret;
//
//    @Value("${dpauc.oauth.jjwt.expiration.access}")
//    private String expirationTime;
//
//    @Value("${dpauc.oauth.jjwt.expiration.refresh}")
//    private String expirationRefreshTime;
//
//    public Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        return getAllClaimsFromToken(token).getExpiration();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
////    public String generateToken(UserDetails userDetails) {
////        AdminDetails adminDetails = (AdminDetails)userDetails;
////        Map<String, Object> claims = new HashMap<>();
////        claims.put("role", adminDetails.getRoleList().get(0).getRoleName());
////        return doGenerateToken(claims, adminDetails.getUsername());
////    }
//
//    public String generateToken(UserDetails userDetails,TokenType tokenType,String tokenId) {
//        AdminDetails adminDetails = (AdminDetails)userDetails;
//        Map<String, Object> claims = new HashMap<>();
////        claims.put("token_type",tokenType.name());
//        claims.put("token_id",tokenId);
//        claims.put("role", adminDetails.getRoleList().get(0).getRoleName());
//        return doGenerateToken(claims, adminDetails.getUsername(),tokenType);
//    }
//
//    private String doGenerateToken(Map<String, Object> claims, String username,TokenType tokenType) {
//        String expireTime = tokenType == TokenType.ACCESS ? expirationTime: expirationRefreshTime;
//        long expirationTimeLong = Long.parseLong(expireTime); //in second
//
//        final Date createdDate = new Date();
//        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(createdDate)
//                .setExpiration(expirationDate)
//                .setIssuer(tokenType.name())
//                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
//                .compact();
//    }
//
//    public Boolean validateToken(String token) {
//        return !isTokenExpired(token);
//    }
//
//    public boolean validateAccessToken(String token){
//        return TokenType.ACCESS.name().equals(getAllClaimsFromToken(token).getIssuer());
//    }
//
//    public boolean validateRefreshToken(String token){
//        return TokenType.REFRESH.name().equals(getAllClaimsFromToken(token).getIssuer());
//    }
//
//    public String createAccessTokenByRefreshToken(String refreshToken){
//        if(validateRefreshToken(refreshToken) && validateToken(refreshToken)){
//            long expirationTimeLong = Long.parseLong(expirationTime); //in second
//            final Date createdDate = new Date();
//            final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
//
//            Claims claims = getAllClaimsFromToken(refreshToken);
//
//            String username = claims.getSubject();
//            Map<String, Object> newClaims = new HashMap<>();
//            claims.forEach(newClaims::put);
//
//            return Jwts.builder()
//                    .setClaims(newClaims)
//                    .setSubject(username)
//                    .setIssuedAt(createdDate)
//                    .setExpiration(expirationDate)
//                    .setIssuer(TokenType.ACCESS.name())
//                    .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
//                    .compact();
//        }
//        return null;
//    }
//
//}
