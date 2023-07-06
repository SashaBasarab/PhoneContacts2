package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;
import com.example.phonecontacts2.repository.RoleRepository;
import com.example.phonecontacts2.repository.UserRepository;
import com.example.phonecontacts2.security.jwt.JwtUtils;
import com.example.phonecontacts2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginAndSignupRequest loginAndSignupRequest) {
        return null;
    }

    @Override
    public void registerUser(LoginAndSignupRequest loginAndSignupRequest) {

    }
}
