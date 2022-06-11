package com.example.mytestdemo.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contacts")
    public ResponseEntity getAllContacts(){
        List<Contact> contactList = contactRepository.getAllContacts();
        return ResponseEntity.ok(contactList);
    }

    @PostMapping ("/contacts")
    public ResponseEntity saveContact(@RequestBody Contact contact){
        Contact savedContact = contactRepository.addContact(contact);
        return ResponseEntity.ok(contact);
    }

}
