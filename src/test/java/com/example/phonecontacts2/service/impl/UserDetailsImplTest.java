package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.enums.UserRole;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsImplTest {

    @Test
    public void testGetAuthorities() {
        Role role1 = new Role(UserRole.USER_ROLE);
        Role role2 = new Role(UserRole.ADMIN_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setPassword("testpassword");
        user.setRoles(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        Assert.assertNotNull(authorities);
        Assert.assertEquals(2, authorities.size());
        Assert.assertTrue(authorities.contains(new SimpleGrantedAuthority("USER_ROLE")));
        Assert.assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN_ROLE")));
    }

    @Test
    public void testGetPassword() {
        String password = "testpassword";
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", password, Collections.emptyList());

        String actualPassword = userDetails.getPassword();

        Assert.assertEquals(password, actualPassword);
    }

    @Test
    public void testGetUsername() {
        String username = "testuser";
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, username, "testpassword", Collections.emptyList());

        String actualUsername = userDetails.getUsername();

        Assert.assertEquals(username, actualUsername);
    }


}
