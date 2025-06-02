package com.springBootBlogapis.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JWTTokenProvider JWTtokenprovider;


    @Autowired
    private CustomUserManager customusermanager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //step 1 : get jwt from HTTP request
        String token = getJWTfromRequest(request);
        //step 2 : validate the token
        if(StringUtils.hasText(token) && JWTtokenprovider.validateToken(token)){
            //step 3 : get username from the token if validateToken returns true
            String username = JWTtokenprovider.getusernameJWT(token);

            //step 4 : load user associated with the token
            UserDetails userdetails = customusermanager.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userdetails, null , userdetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //step 5 : set spring security

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);



        }

        filterChain.doFilter(request , response);




    }

    //Bearer <accessToken>
    private String getJWTfromRequest (HttpServletRequest request){
        String TokenBearer = request.getHeader("Authorization");
        if(StringUtils.hasText(TokenBearer) && TokenBearer.startsWith("Bearer ")){
            return TokenBearer.substring(7 , TokenBearer.length());

        }
        return null;

    }

}
