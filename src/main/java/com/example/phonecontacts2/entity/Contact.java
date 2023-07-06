package com.example.phonecontacts2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
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

//    @JsonIgnore
    @ManyToOne
    private User ownerOfContact;

}
