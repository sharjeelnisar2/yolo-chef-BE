package com.yolo.chef.dto;

import lombok.Getter;

@Getter
public class CreateUserProfileRequest {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String house;
    private String street;
    private String area;
    private String zipCode;
    private String city;
    private String country;
    private String currencyCode;

    @Override
    public String toString() {
        return "CreateUserProfileRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", house='" + house + '\'' +
                ", street='" + street + '\'' +
                ", area='" + area + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
