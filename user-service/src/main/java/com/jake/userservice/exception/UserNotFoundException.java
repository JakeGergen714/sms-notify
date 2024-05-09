package com.jake.userservice.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("No User found with ID <%s>", userId));
    }

    public UserNotFoundException(String username) {
        super(String.format("No User found with username <%s>", username));
    }
}
