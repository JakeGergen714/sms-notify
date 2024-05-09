package com.jake.userservice.exception;

public class UserAlreadyOwnsABusiness extends RuntimeException {

    public UserAlreadyOwnsABusiness(String username, Long businessId) {
        super(String.format("User <%s> already owns a business with ID <%s>", username, businessId));
    }
}
