package com.aca.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Address extends AbstractEntity {

    private String country;

    private String city;

    public Address(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.country = resultSet.getString("country");
        this.city = resultSet.getString("city");
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}