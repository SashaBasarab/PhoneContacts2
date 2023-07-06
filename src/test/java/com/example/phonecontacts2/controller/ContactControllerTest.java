package com.example.phonecontacts2.controller;

import com.example.phonecontacts2.entity.Contact;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewContact() {
        Long userId = 1L;
        Contact contact = new Contact();

        Mockito.doNothing().when(contactService).addContact(Mockito.any(Contact.class), Mockito.eq(userId));

        contactController.addNewContact(userId, contact);

        Mockito.verify(contactService).addContact(contact, userId);
    }

    @Test
    public void testUpdateContact() {
        Long contactId = 1L;
        Long userId = 1L;
        Contact contact = new Contact();

        Mockito.doNothing().when(contactService).updateContact(Mockito.eq(contactId), Mockito.any(Contact.class), Mockito.eq(userId));

        contactController.updateContact(contactId,userId, contact);

        Mockito.verify(contactService).updateContact(contactId, contact, userId);
    }

    @Test
    public void testDeleteContact() {
        Long contactId = 1L;

        Mockito.doNothing().when(contactService).deleteContact(Mockito.eq(contactId));

        contactController.deleteContact(contactId);

        Mockito.verify(contactService).deleteContact(contactId);
    }

    @Test
    public void testGetContacts() {
        Long userId = 1L;
        List<Contact> expectedContacts = new ArrayList<>();

        Mockito.when(contactService.getAllContacts(Mockito.eq(userId))).thenReturn(expectedContacts);

        List<Contact> result = contactController.getContacts(userId);

        Mockito.verify(contactService).getAllContacts(userId);

        Assert.assertEquals(expectedContacts, result);
    }
}