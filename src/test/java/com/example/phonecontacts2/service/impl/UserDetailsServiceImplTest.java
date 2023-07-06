package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.enums.UserRole;
import com.example.phonecontacts2.exception.NoSuchUserException;
import com.example.phonecontacts2.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_ExistingUser() {
        // Arrange
        String login = "testuser";
        User user = new User();
        user.setLogin(login);
        user.setPassword("testpassword");
        Role role = new Role(UserRole.USER_ROLE);
        user.getRoles().add(role);

        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);

        // Assert
        Assert.assertNotNull(userDetails);
        Assert.assertEquals(login, userDetails.getUsername());
        Assert.assertEquals(user.getPassword(), userDetails.getPassword());
        Assert.assertEquals(1, userDetails.getAuthorities().size());
        Assert.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER_ROLE")));

        Mockito.verify(userRepository, Mockito.times(1)).findByLogin(login);
    }

    @Test(expected = NoSuchUserException.class)
    public void testLoadUserByUsername_NonExistingUser() {
        // Arrange
        String login = "testuser";

        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        userDetailsService.loadUserByUsername(login);

        // The NoSuchUserException should be thrown

        Mockito.verify(userRepository, Mockito.times(1)).findByLogin(login);
    }

}
