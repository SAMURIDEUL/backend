package com.example.samuL.user.service;


import com.example.samuL.user.dto.UserDto;
import com.example.samuL.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto user = userMapper.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new CustomUserDetails(user.getEmail(), user.getPassword_hash());

    }

}
