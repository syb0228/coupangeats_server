package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhoneNum;
    private String status;
}
