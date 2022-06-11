package com.example.mytestdemo.contact;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Contact {

    private String name;
    private String surname;
    private String phoneNo;

}
