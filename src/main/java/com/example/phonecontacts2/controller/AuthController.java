package com.example.phonecontacts2.controller;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;
import com.example.phonecontacts2.dto.response.MessageResponse;
import com.example.phonecontacts2.exception.UserAlreadyExists;
import com.example.phonecontacts2.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginAndSignupRequest loginAndSignupRequest) {
        log.info("request: {}", loginAndSignupRequest);
        return ResponseEntity.ok(authService.authenticateUser(loginAndSignupRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid LoginAndSignupRequest loginAndSignupRequest) {
        log.info("request: {}", loginAndSignupRequest);
        try {
            authService.registerUser(loginAndSignupRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (UserAlreadyExists e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: login or password are already taken!"));
        }
    }

}
