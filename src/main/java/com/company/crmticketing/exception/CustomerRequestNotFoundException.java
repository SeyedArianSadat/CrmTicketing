package com.company.crmticketing.exception;

public class CustomerRequestNotFoundException extends RuntimeException {
    public CustomerRequestNotFoundException(Long customerRequestId) {
        super("CustomerRequest with id " + customerRequestId + " not found");
    }
}
