package com.aca.service;

public class AddressCreationParameter {

    private final Long userId;

    private final String country;

    private final String city;

    public AddressCreationParameter(Long userId, String country, String city) {
        this.userId = userId;
        this.country = country;
        this.city = city;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}