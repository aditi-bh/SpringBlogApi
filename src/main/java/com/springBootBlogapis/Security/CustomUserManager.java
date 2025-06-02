package com.springBootBlogapis.Security;

import com.springBootBlogapis.Entity.Role;
import com.springBootBlogapis.Entity.User;
import com.springBootBlogapis.Repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomUserManager implements UserDetailsService {

    private UserRepo userRepository;

    public CustomUserManager(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

       return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
               maprolestoauthority(user.getRoles()));


    }


    private Collection<? extends GrantedAuthority> maprolestoauthority(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());

    }
}
