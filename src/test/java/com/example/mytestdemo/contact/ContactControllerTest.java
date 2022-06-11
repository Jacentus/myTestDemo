package com.example.mytestdemo.contact;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        contactRepository.addContact(new Contact("Jacek", "Motyka", "123456789"));
        contactRepository.addContact(new Contact("Walentyna", "Motyka-Ćwierz", "987654321"));
    }

    @Test
    @DisplayName("GET /contacts returns HTTP 200")
    void when_GetAllContacts_then_return_HTTP200() throws Exception {
        String endpointURL = "/contacts";
        mockMvc.perform(get(endpointURL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /contact returns HTTP 404")
    void when_GetAllContacts_withInvalidEndpointURL_then_return_HTTP404() throws Exception {
        String endpointURL = "/contact";
        mockMvc.perform(get(endpointURL))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /contacts returns proper size of List")
    void when_GetAllContacts_then_return_valid_list_size() throws Exception {
        String endpointURL = "/contacts";
        MvcResult result = mockMvc.perform(get(endpointURL))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn();

        String contactsAsJSON = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        List<Contact> contactList = objectMapper.readValue(contactsAsJSON, new TypeReference<>(){});

        assertEquals(2, contactList.size());
    }

    @Test
    @DisplayName("GET /contacts returns list of all Contacts (2)")
    void when_GetAllContacts_then_return_allContacts() throws Exception {
        String endpointURL = "/contacts";
        MvcResult result = mockMvc.perform(get(endpointURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contactsAsJSON = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        List<Contact> contactList = objectMapper.readValue(contactsAsJSON, new TypeReference<>(){});

        assertAll("All contacts from ContactRepository",
                () -> assertEquals("Jacek", contactList.get(0).getName()),
                () -> assertEquals("Walentyna", contactList.get(1).getName()));
    }

    @Test
    @DisplayName("POST /contacts adds Contact to repository")
    void when_AddContact_then_save__and_HTTP200() throws Exception {
        //given
        String endpointURL = "/contacts";
        String name = "XXX";
        String surname = "YYY";
        String phoneNo = "666666666";
        Contact newContact = Contact.builder().name(name).surname(surname).phoneNo(phoneNo).build();
        String newContactAsJSON = objectMapper.writeValueAsString(newContact);
        //when
        mockMvc.perform(get(endpointURL))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("POST /contacts adds Contact to repository and returns saved Contact")
    void when_AddContact_then_save_return_saved_Contact_and_HTTP200() throws Exception {
        //given
        String endpointURL = "/contacts";
        String name = "XXX";
        String surname = "YYY";
        String phoneNo = "666666666";
        Contact newContact = Contact.builder().name(name).surname(surname).phoneNo(phoneNo).build();
        String newContactAsJSON = objectMapper.writeValueAsString(newContact);

        //when
        MvcResult mvcResult = mockMvc.perform(post(endpointURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newContactAsJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String savedContactAsJSON = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Contact savedContact = objectMapper.readValue(savedContactAsJSON, Contact.class);
        //then
        assertEquals(newContact, savedContact);
    }


}