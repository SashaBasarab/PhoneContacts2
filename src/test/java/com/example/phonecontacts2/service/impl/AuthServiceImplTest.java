package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;
import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.enums.UserRole;
import com.example.phonecontacts2.exception.UserAlreadyExists;
import com.example.phonecontacts2.repository.RoleRepository;
import com.example.phonecontacts2.repository.UserRepository;
import com.example.phonecontacts2.security.jwt.JwtUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticateUser() {
        String login = "testuser";
        String password = "testpassword";
        LoginAndSignupRequest loginAndSignupRequest = new LoginAndSignupRequest();
        loginAndSignupRequest.setLogin(login);
        loginAndSignupRequest.setPassword(password);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "testjwt";
        List< GrantedAuthority > authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER_ROLE.name()));
                UserDetailsImpl userDetails = new UserDetailsImpl(1L, login, password, authorities);
        List<String> roles = Collections.singletonList("USER_ROLE");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Mockito.when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class))).thenReturn(jwt);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        JwtResponse result = authService.authenticateUser(loginAndSignupRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(jwt, result.getToken());
        Assert.assertEquals(userDetails.getId(), result.getId());
        Assert.assertEquals(userDetails.getUsername(), result.getLogin());
        Assert.assertEquals(roles, result.getRoles());
    }



    @Test
    public void testRegisterUser() {
        String login = "testuser";
        String password = "testpassword";
        LoginAndSignupRequest loginAndSignupRequest = new LoginAndSignupRequest();
        loginAndSignupRequest.setLogin(login);
        loginAndSignupRequest.setPassword(password);
        Role userRole = new Role(UserRole.USER_ROLE);
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        Mockito.when(userRepository.existsByLogin(login)).thenReturn(false);
        Mockito.when(roleRepository.findByName(UserRole.USER_ROLE)).thenReturn(Optional.of(userRole));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        authService.registerUser(loginAndSignupRequest);

        Mockito.verify(userRepository, Mockito.times(1)).existsByLogin(login);
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(UserRole.USER_ROLE);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test(expected = UserAlreadyExists.class)
    public void testRegisterUser_UserAlreadyExists() {
        String login = "testuser";
        String password = "testpassword";
        LoginAndSignupRequest loginAndSignupRequest = new LoginAndSignupRequest();
        loginAndSignupRequest.setLogin(login);
        loginAndSignupRequest.setPassword(password);

        Mockito.when(userRepository.existsByLogin(login)).thenReturn(true);

        authService.registerUser(loginAndSignupRequest);

    }
}
