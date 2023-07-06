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
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Contact> expectedContacts = new ArrayList<>();
        expectedContacts.add(new Contact());
        expectedContacts.add(new Contact());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(expectedContacts);

        List<Contact> actualContacts = contactService.getAllContacts(userId);

        Assert.assertEquals(expectedContacts, actualContacts);
    }

    @Test
    public void testGetContactById() {
        Long contactId = 1L;
        Contact expectedContact = new Contact();
        expectedContact.setId(contactId);

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(expectedContact));

        Contact actualContact = contactService.getContactById(contactId);

        Assert.assertEquals(expectedContact, actualContact);
    }

    @Test
    public void testAddContact() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Contact contact = new Contact();
        contact.setOwnerOfContact(user);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(Collections.emptyList());

        contactService.addContact(contact, userId);

        Mockito.verify(contactRepository).save(contact);
    }

    @Test(expected = UserWithProvidedContactNameAlreadyExists.class)
    public void testAddContactWithExistingName() {
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

        contactService.addContact(contact, userId);
    }


}
