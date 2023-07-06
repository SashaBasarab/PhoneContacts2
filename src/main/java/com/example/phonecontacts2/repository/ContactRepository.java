package com.example.phonecontacts2.repository;

import com.example.phonecontacts2.entity.Contact;
import com.example.phonecontacts2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByOwnerOfContact(User user);
}
