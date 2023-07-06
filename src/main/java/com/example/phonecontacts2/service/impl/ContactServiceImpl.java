package com.example.phonecontacts2.service.impl;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.exception.*;
import com.example.phonecontacts2.repository.ContactRepository;
import com.example.phonecontacts2.repository.UserRepository;
import com.example.phonecontacts2.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Contact> getAllContacts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException("User with id: " + userId + " does not exist!"));
        System.out.println(Arrays.toString(contactRepository.findAllByOwnerOfContact(user).toArray()));
        return contactRepository.findAllByOwnerOfContact(user);
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    //I could just use @UniqueElements in Contact class, but this solution provides unique elements for each user instead of all users. In my opinion this is better approach
    @Override
    public void addContact(Contact contact, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException("User with id: " + userId + " does not exist!"));
        contact.setOwnerOfContact(user);
        List<Contact> existingContactsOfUser = contactRepository.findAllByOwnerOfContact(user);
        for (Contact existingContact : existingContactsOfUser) {
            if (existingContact.getName().equals(contact.getName())) {
                throw new UserWithProvidedContactNameAlreadyExists("User with contact name: " + contact.getName() + " already exists!");
            }
            for (String existingEmail : existingContact.getEmails()) {
                for (String requestEmail: contact.getEmails()) {
                    if (existingEmail.equals(requestEmail)) {
                        throw new UserWithProvidedEmailsAlreadyExists("User with email: " + requestEmail + " already exists!");
                    }
                }
            }
            for (String existingPhoneNumber : existingContact.getPhoneNumbers()) {
                for (String requestPhoneNumber: contact.getPhoneNumbers()) {
                    if (existingPhoneNumber.equals(requestPhoneNumber)) {
                        throw new UserWithProvidedPhoneNumbersAlreadyExists("User with phone number: " + requestPhoneNumber + " already exists!");
                    }
                }
            }
        }
        contactRepository.save(contact);
    }

    @Override
    public void updateContact(Long contactId, Contact contact, Long userId) {
        Contact updatedContact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NoSuchContactException("Contact with id: " + contactId + " does not exist!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException("User with id: " + userId + " does not exist!"));
        List<Contact> existingContactsOfUser = contactRepository.findAllByOwnerOfContact(user);
        for (Contact existingContact : existingContactsOfUser) {
            if (existingContact.getName().equals(contact.getName())) {
                throw new UserWithProvidedContactNameAlreadyExists("User with contact name: " + contact.getName() + " already exists!");
            }
            for (String existingEmail : existingContact.getEmails()) {
                for (String requestEmail: contact.getEmails()) {
                    if (existingEmail.equals(requestEmail)) {
                        throw new UserWithProvidedEmailsAlreadyExists("User with email: " + requestEmail + " already exists!");
                    }
                }
            }
            for (String existingPhoneNumber : existingContact.getPhoneNumbers()) {
                for (String requestPhoneNumber: contact.getPhoneNumbers()) {
                    if (existingPhoneNumber.equals(requestPhoneNumber)) {
                        throw new UserWithProvidedPhoneNumbersAlreadyExists("User with phone number: " + requestPhoneNumber + " already exists!");
                    }
                }
            }
        }
        updatedContact.setName(contact.getName());
        updatedContact.setEmails(contact.getEmails());
        updatedContact.setPhoneNumbers(contact.getPhoneNumbers());
        updatedContact.setOwnerOfContact(user);
        contactRepository.save(updatedContact);

    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

}
