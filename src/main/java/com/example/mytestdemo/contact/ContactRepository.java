package com.example.mytestdemo.contact;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactRepository {

    private List<Contact> contactList;

    public ContactRepository(List<Contact> contactList) {
        this.contactList = new ArrayList<>();
        //this.populateDb();
    }

    private void populateDb() {
        this.contactList.add(new Contact("Jacek", "Motyka", "123456789"));
        this.contactList.add(new Contact("Walentyna", "Motyka-Ćwierz", "987654321"));
    }

    public Contact addContact(Contact contact) {
        contactList.add(contact);
        return contact;
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

}
