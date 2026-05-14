package com.company.crmticketing.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long CustomerId) {
        super("Customer with id " + CustomerId + " not found");
    }
}
