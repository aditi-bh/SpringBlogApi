package com.springBootBlogapis.Security;


import com.springBootBlogapis.Exceptions.BlogAPIException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-aspiration-milisecond}")
    private int JwtExpinMs;

    //Generate Token Method
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JwtExpinMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }

    //get username from the token

    public String getusernameJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();


    }

    //validate JWT token
    public Boolean validateToken(String token){

        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch(SignatureException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "invalid jwt signature");

        }catch(MalformedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "invalid jwt token");

        }catch(ExpiredJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "expired jwt token");

        }catch(UnsupportedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "unsupported jwt token");

        }catch(IllegalArgumentException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "jst cliams string is empty");

        }




    }


}
