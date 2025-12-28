package com.n23.expense_sharing_app.exception;

public class ResourceNotFoundException extends RuntimeException{

    // constructor
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
