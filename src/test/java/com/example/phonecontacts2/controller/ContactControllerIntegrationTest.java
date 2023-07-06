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
        // Ініціалізуємо дані для тестів
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
        // Arrange
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
//        String base64Credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Contact> requestEntity = new HttpEntity<>(contact, headers);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(getBaseUrl() + "/add-new-contact?userId=" + userId,
                requestEntity, Void.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Перевіряємо, чи контакт був збережений в базі даних
        List<Contact> savedContacts = contactRepository.findAllByOwnerOfContact(userRepository.findById(userId).get());
        assertEquals(1, savedContacts.size());
        // Перевіряємо, чи дані контакту співпадають з вхідними даними
        Contact savedContact = savedContacts.get(0);
        assertEquals(contact.getName(), savedContact.getName());
        // ...
    }

    @Test
    public void testUpdateContact() {
        // Arrange
        Long contactId = 1L;
        Contact existingContact = new Contact();
        // Заповнюємо дані існуючого контакту
        // ...
        contactRepository.save(existingContact);

        Contact updatedContact = new Contact();
        // Заповнюємо дані оновленого контакту
        // ...

        // Act
        restTemplate.put(getBaseUrl() + "/edit-contact?contactId=" + contactId,
                updatedContact);

        // Assert
        // Перевіряємо, чи контакт був оновлений в базі даних
        Contact savedContact = contactRepository.findById(contactId).orElse(null);
        assertNotNull(savedContact);
        // Перевіряємо, чи дані контакту співпадають з оновленими даними
        assertEquals(updatedContact.getName(), savedContact.getName());
        // ...
    }

}