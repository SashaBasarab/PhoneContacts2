package com.example.phonecontacts2.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ElementCollection
    private List<@Email String> emails;

    @ElementCollection
    private List<@Pattern(regexp = "\\+380\\d{9}", message = "Phone number must be in the format +380XXXXXXXXX")String> phoneNumbers;

    @ManyToOne
    private User ownerOfContact;

}
