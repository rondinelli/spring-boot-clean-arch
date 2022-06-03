package com.stt.solution.application.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;


        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            roles = Arrays.asList(new SimpleGrantedAuthority(getRoleFromUserType( user.get().getUserTypeName())));
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found with the name " + username);
    }

    private String getRoleFromUserType(String userType){
        if(userType == null || userType.equals("student")){
            return "ROLE_STUDENT";
        }else if( userType.equals("admin")){
            return "ROLE_ADMIN";
        }else if(userType.equals("teacher")){
            return "ROLE_TEACHER";
        }

        return "";
    }


}
