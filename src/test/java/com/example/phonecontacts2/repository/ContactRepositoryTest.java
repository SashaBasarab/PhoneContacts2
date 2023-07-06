package com.example.phonecontacts2.repository;

import com.example.phonecontacts2.controller.ContactController;
import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.service.ContactService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ContactRepositoryTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllByOwnerOfContact() {
        // Arrange
        User user = new User();
        // Set up any necessary data for the test

        List<Contact> expectedContacts = new ArrayList<>();
        // Set up the expected contacts list

        // Mock the contactRepository method
        Mockito.when(contactRepository.findAllByOwnerOfContact(Mockito.eq(user))).thenReturn(expectedContacts);

        // Act
        List<Contact> result = contactRepository.findAllByOwnerOfContact(user);

        // Assert
        Assert.assertEquals(expectedContacts, result);
        Mockito.verify(contactRepository).findAllByOwnerOfContact(user);
    }
}