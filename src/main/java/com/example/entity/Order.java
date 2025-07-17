package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order {

    private String name;

    private String surname;

    private String phone;

    private String email;

    private String street;

    private String city;

    private String country;

    private String postcode;

}
