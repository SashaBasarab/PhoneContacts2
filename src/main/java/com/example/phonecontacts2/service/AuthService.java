package com.example.phonecontacts2.service;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginAndSignupRequest loginAndSignupRequest);

    void registerUser(LoginAndSignupRequest loginAndSignupRequest);

}
