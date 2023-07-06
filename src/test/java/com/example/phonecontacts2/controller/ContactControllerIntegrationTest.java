package com.example.phonecontacts2.controller;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.Role;
import com.example.phonecontacts2.entity.User;
import com.example.phonecontacts2.enums.UserRole;
import com.example.phonecontacts2.repository.ContactRepository;
import com.example.phonecontacts2.repository.RoleRepository;
import com.example.phonecontacts2.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @LocalServerPort
    private int port = 8080;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/contacts";
    }

    @Before
    public void setup() {
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("testpassword");
        user.setPassword(encodedPassword);
        Role role = new Role(UserRole.USER_ROLE);
        roleRepository.save(role);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @After
    public void cleanup() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testAddNewContact() {
        Long userId = 1L;
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setName("TestContact");
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380950055258");
        contact.setPhoneNumbers(phoneNumbers);
        List<String> emails = new ArrayList<>();
        emails.add("testemail@gmail.com");
        contact.setEmails(emails);
        contact.setOwnerOfContact(userRepository.getById(userId));

        String username = "testuser";
        String password = "testpassword";
        String secretKey = "finalProject";
        String token = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Contact> requestEntity = new HttpEntity<>(contact, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(getBaseUrl() + "/add-new-contact?userId=" + userId,
                requestEntity, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Contact> savedContacts = contactRepository.findAllByOwnerOfContact(userRepository.findById(userId).get());
        assertEquals(1, savedContacts.size());
        Contact savedContact = savedContacts.get(0);
        assertEquals(contact.getName(), savedContact.getName());
    }

    @Test
    public void testUpdateContact() {
        Long contactId = 1L;
        Contact existingContact = new Contact();
        Long userId = 1L;
        existingContact.setId(1L);
        existingContact.setName("TestContact");
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380950055258");
        existingContact.setPhoneNumbers(phoneNumbers);
        List<String> emails = new ArrayList<>();
        emails.add("testemail@gmail.com");
        existingContact.setEmails(emails);
        existingContact.setOwnerOfContact(userRepository.getById(userId));
        contactRepository.save(existingContact);

        Contact updatedContact = new Contact();

        updatedContact.setId(existingContact.getId());
        updatedContact.setName("TestContact2");
        List<String> newPhoneNumbers = new ArrayList<>();
        newPhoneNumbers.add("+380950055254");
        updatedContact.setPhoneNumbers(newPhoneNumbers);
        List<String> newEmails = new ArrayList<>();
        newEmails.add("testemailll@gmail.com");
        updatedContact.setEmails(newEmails);
        updatedContact.setOwnerOfContact(existingContact.getOwnerOfContact());
        String username = "testuser";
        String password = "testpassword";
        String secretKey = "finalProject";
        String token = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Contact> requestEntity = new HttpEntity<>(updatedContact, headers);

        restTemplate.put(getBaseUrl() + "/edit-contact?contactId=" + contactId + "&userId=" + userId,
                requestEntity);

        Contact savedContact = contactRepository.findById(contactId).orElse(null);
        assertNotNull(savedContact);
        assertEquals(updatedContact.getName(), savedContact.getName());
    }

}