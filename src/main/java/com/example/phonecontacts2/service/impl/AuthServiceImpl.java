package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.dto.request.LoginAndSignupRequest;
import com.example.phonecontacts2.dto.response.JwtResponse;
import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.enums.UserRole;
import com.example.phonecontacts2.exception.RoleNotFoundException;
import com.example.phonecontacts2.exception.UserAlreadyExists;
import com.example.phonecontacts2.repository.RoleRepository;
import com.example.phonecontacts2.repository.UserRepository;
import com.example.phonecontacts2.security.jwt.JwtUtils;
import com.example.phonecontacts2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginAndSignupRequest.getLogin(), loginAndSignupRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setLogin(userDetails.getUsername());
        jwtResponse.setRoles(roles);
        return jwtResponse;
    }

    @Override
    public void registerUser(LoginAndSignupRequest loginAndSignupRequest) throws UserAlreadyExists{
        if (userRepository.existsByLogin(loginAndSignupRequest.getLogin())) {
            throw new UserAlreadyExists("User with login: " + loginAndSignupRequest.getLogin() + " already exists");
        }
        User user = new User();
        user.setLogin(loginAndSignupRequest.getLogin());
        user.setPassword(encoder.encode(loginAndSignupRequest.getPassword()));
        Role userRole = roleRepository.findByName(UserRole.USER_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Error: role is not found."));
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

}
