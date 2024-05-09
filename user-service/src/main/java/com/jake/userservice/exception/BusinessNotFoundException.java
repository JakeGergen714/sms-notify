package com.jake.userservice.exception;

public class BusinessNotFoundException extends ResourceNotFoundException {
    public BusinessNotFoundException(Long businessId) {
        super(String.format("No Business found with ID <%s>", businessId));
    }
}
