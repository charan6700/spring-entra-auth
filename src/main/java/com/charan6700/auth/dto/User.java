package com.charan6700.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private List<String> businessPhones;
    private String displayName;
    private String givenName;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String officeLocation;
    private String preferredLanguage;
    private String surname;
    private String userPrincipalName;
    private String id;
}
