package com.aca.db.exeption;

public class EmailAlreadyExistsException extends RuntimeException {

    private final String email;

    public EmailAlreadyExistsException(String email) {
        super("Email already exists. Email: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
