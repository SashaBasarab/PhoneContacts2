package com.example.phonecontacts2.controller;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;
import com.example.phonecontacts2.dto.response.MessageResponse;
import com.example.phonecontacts2.exception.UserAlreadyExists;
import com.example.phonecontacts2.service.AuthService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticateUser() {
        LoginAndSignupRequest request = new LoginAndSignupRequest();

        JwtResponse expectedResponse = new JwtResponse();
        Mockito.when(authService.authenticateUser(Mockito.eq(request))).thenReturn(expectedResponse);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(request);

        Mockito.verify(authService).authenticateUser(request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testRegisterUser() {
        LoginAndSignupRequest request = new LoginAndSignupRequest();

        Mockito.doNothing().when(authService).registerUser(Mockito.eq(request));

        ResponseEntity<?> response = authController.registerUser(request);

        Mockito.verify(authService).registerUser(request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        LoginAndSignupRequest request = new LoginAndSignupRequest();

        Mockito.doThrow(UserAlreadyExists.class).when(authService).registerUser(Mockito.eq(request));

        ResponseEntity<?> response = authController.registerUser(request);

        Mockito.verify(authService).registerUser(request);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Error: login or password are already taken!", ((MessageResponse) response.getBody()).getMessage());
    }
}