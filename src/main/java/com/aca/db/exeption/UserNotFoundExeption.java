package com.aca.db.exeption;

public class UserNotFoundExeption extends RuntimeException {
    private final Long userId;

    public UserNotFoundExeption(Long userId) {
        super(String.format("User not found for id: %s", userId));
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
