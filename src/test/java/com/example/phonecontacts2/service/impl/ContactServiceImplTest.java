package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.repository.ContactRepository;
import com.example.phonecontacts2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
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
    void getAllContacts() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setOwnerOfContact(user);
        contact.getPhoneNumbers().add("+380950055258");
        contact.getEmails().add("qdfafa@gmail.com");
        contact.setName("Sasha");
        contact.setId(1L);
        // Ініціалізуйте контакт з потрібними значеннями

        List<Contact> existingContactsOfUser = new ArrayList<>();
        existingContactsOfUser.add(contact);
        // Додайте існуючі контакти для користувача до existingContactsOfUser

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(contactRepository.findAllByOwnerOfContact(user)).thenReturn(existingContactsOfUser);
        Mockito.when(contactRepository.save(contact)).thenReturn(contact);

        // Act
        contactService.addContact(contact, userId);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(contactRepository, Mockito.times(1)).findAllByOwnerOfContact(user);
        Mockito.verify(contactRepository, Mockito.times(1)).save(contact);
    }

    @Test
    void addContact() {
    }

    @Test
    void updateContact() {
    }

    @Test
    void deleteContact() {
    }
}