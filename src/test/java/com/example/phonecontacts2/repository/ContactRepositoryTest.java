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
        User user = new User();
        user.setId(1L);

        List<Contact> expectedContacts = new ArrayList<>();
        Contact contact = new Contact();
        contact.setOwnerOfContact(user);
        contactRepository.save(contact);
        expectedContacts.add(contact);

        Mockito.when(contactRepository.findAllByOwnerOfContact(Mockito.eq(user))).thenReturn(expectedContacts);

        List<Contact> result = contactRepository.findAllByOwnerOfContact(user);

        Assert.assertEquals(expectedContacts, result);
        Mockito.verify(contactRepository).findAllByOwnerOfContact(user);
        contactRepository.delete(contact);
    }
}