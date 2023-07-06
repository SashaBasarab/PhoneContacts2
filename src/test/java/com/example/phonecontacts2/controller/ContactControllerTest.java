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

import static org.junit.jupiter.api.Assertions.*;

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
        // Set up any necessary data for the test

        // Mock the contactService method
        Mockito.doNothing().when(contactService).addContact(Mockito.any(Contact.class), Mockito.eq(userId));

        // Perform the request
        contactController.addNewContact(userId, contact);

        // Verify that the contactService method was called with the expected arguments
        Mockito.verify(contactService).addContact(contact, userId);
    }

    @Test
    public void testUpdateContact() {
        Long contactId = 1L;
        Contact contact = new Contact();
        // Set up any necessary data for the test

        // Mock the contactService method
        Mockito.doNothing().when(contactService).updateContact(Mockito.eq(contactId), Mockito.any(Contact.class));

        // Perform the request
        contactController.updateContact(contactId, contact);

        // Verify that the contactService method was called with the expected arguments
        Mockito.verify(contactService).updateContact(contactId, contact);
    }

    @Test
    public void testDeleteContact() {
        Long contactId = 1L;
        // Set up any necessary data for the test

        // Mock the contactService method
        Mockito.doNothing().when(contactService).deleteContact(Mockito.eq(contactId));

        // Perform the request
        contactController.deleteContact(contactId);

        // Verify that the contactService method was called with the expected argument
        Mockito.verify(contactService).deleteContact(contactId);
    }

    @Test
    public void testGetContacts() {
        Long userId = 1L;
        List<Contact> expectedContacts = new ArrayList<>();
        // Set up any necessary data for the test

        // Mock the contactService method
        Mockito.when(contactService.getAllContacts(Mockito.eq(userId))).thenReturn(expectedContacts);

        // Perform the request
        List<Contact> result = contactController.getContacts(userId);

        // Verify that the contactService method was called with the expected argument
        Mockito.verify(contactService).getAllContacts(userId);

        // Verify the result
        Assert.assertEquals(expectedContacts, result);
    }
}