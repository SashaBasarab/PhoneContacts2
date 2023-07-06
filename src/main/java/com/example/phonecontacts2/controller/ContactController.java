package com.example.phonecontacts2.controller;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contacts")
@Slf4j
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/add-new-contact")
    public void addNewContact(@RequestParam Long userId,
            @Valid @RequestBody Contact contact) {
        log.info("\nRequest: Name: {},\nEmails: {}, \nphoneNumbers: {}, \nuserId: {}", contact.getName(), contact.getEmails(), contact.getPhoneNumbers(), userId);
        contactService.addContact(contact, userId);
    }

    @PutMapping("/edit-contact")
    public void updateContact(@RequestParam Long contactId,
                              @Valid @RequestBody Contact contact) {
        log.info("\nRequest: Name: {},\nEmails: {}, \nphoneNumbers: {}, \ncontactId: {}", contact.getName(), contact.getEmails(), contact.getPhoneNumbers(), contactId);
        contactService.updateContact(contactId, contact);
    }

    @DeleteMapping("/delete-contact")
    public void deleteContact(@RequestParam Long contactId) {
        log.info("\nRequest: ContactId: {}", contactId);
        contactService.deleteContact(contactId);
    }

    @GetMapping("/get-contacts")
    public List<Contact> getContacts(@RequestParam Long userId) {
        log.info("Request: UserId: {}", userId);
        return contactService.getAllContacts(userId);
    }

}
