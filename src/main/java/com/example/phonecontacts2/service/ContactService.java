package com.example.phonecontacts2.service;

import com.example.phonecontacts2.entity.Contact;

import java.util.List;

public interface ContactService {

    List<Contact> getAllContacts(Long userId);

    Contact getContactById(Long id);

    void addContact(Contact contact, Long userId);

    void updateContact(Long contactId, Contact contact);

    void deleteContact(Long id);

}

