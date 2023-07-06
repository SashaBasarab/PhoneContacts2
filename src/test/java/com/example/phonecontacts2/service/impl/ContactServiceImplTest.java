package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.exception.UserWithProvidedContactNameAlreadyExists;
import com.example.phonecontacts2.repository.ContactRepository;
import com.example.phonecontacts2.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceImplTest {

    @InjectMocks
    private ContactServiceImpl contactService;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetAllContacts() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Contact> expectedContacts = new ArrayList<>();
        expectedContacts.add(new Contact());
        expectedContacts.add(new Contact());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(expectedContacts);

        // Act
        List<Contact> actualContacts = contactService.getAllContacts(userId);

        // Assert
        Assert.assertEquals(expectedContacts, actualContacts);
    }

    @Test
    public void testGetContactById() {
        // Arrange
        Long contactId = 1L;
        Contact expectedContact = new Contact();
        expectedContact.setId(contactId);

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(expectedContact));

        // Act
        Contact actualContact = contactService.getContactById(contactId);

        // Assert
        Assert.assertEquals(expectedContact, actualContact);
    }

    @Test
    public void testAddContact() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Contact contact = new Contact();
        contact.setOwnerOfContact(user);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(Collections.emptyList());

        // Act
        contactService.addContact(contact, userId);

        // Assert
        Mockito.verify(contactRepository).save(contact);
    }

    @Test(expected = UserWithProvidedContactNameAlreadyExists.class)
    public void testAddContactWithExistingName() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Contact contact = new Contact();
        contact.setOwnerOfContact(user);
        contact.setName("ExistingName");

        Contact existingContact = new Contact();
        existingContact.setName("ExistingName");
        List<Contact> existingContacts = Collections.singletonList(existingContact);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(existingContacts);

        // Act
        contactService.addContact(contact, userId);
    }

    // Add more test cases for the other methods in ContactServiceImpl

}
