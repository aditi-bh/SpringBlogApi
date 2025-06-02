package com.springBootBlogapis.Controllers;


import com.springBootBlogapis.Entity.Role;
import com.springBootBlogapis.Entity.User;
import com.springBootBlogapis.Payloads.JWTAuthResponse;
import com.springBootBlogapis.Payloads.LoginDTO;
import com.springBootBlogapis.Payloads.SignUpDTO;
import com.springBootBlogapis.Payloads.UserDTO;
import com.springBootBlogapis.Repository.RoleRepo;
import com.springBootBlogapis.Repository.UserRepo;
import com.springBootBlogapis.Security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserRepo userrepository;


    @Autowired
    private RoleRepo rolerepository;


    @Autowired
    private JWTTokenProvider tokenProvider;






    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO logdto){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(logdto.getUsernameOrEmail(),logdto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get a token from tokenProvider Class
        String token = tokenProvider.generateToken(authentication);


        return ResponseEntity.ok(new JWTAuthResponse(token));

    }




    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody  SignUpDTO signupdto){

        if(userrepository.existsByUsername(signupdto.getUsername())){
            return new ResponseEntity<>("User already exists" , HttpStatus.BAD_REQUEST);
        }

        if(userrepository.existsByEmail(signupdto.getEmail())){
            return new ResponseEntity<>("user already exists" , HttpStatus.BAD_REQUEST);

        }

        User user = new User();
        user.setName(signupdto.getName());
        user.setEmail(signupdto.getEmail());
        user.setUsername(signupdto.getUsername());
        user.setPassword(signupdto.getPassword());

        Role roles = rolerepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return rolerepository.save(newRole);
                });
        user.setRoles(Collections.singleton(roles));


        userrepository.save(user);

        return new ResponseEntity<>("user registered successfully", HttpStatus.OK);



    }

}
