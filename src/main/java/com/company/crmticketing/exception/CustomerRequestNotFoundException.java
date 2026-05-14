package com.company.crmticketing.exception;

public class CustomerRequestNotFoundException extends RuntimeException {
    public CustomerRequestNotFoundException(Long CustomerRequestId) {
        super("CustomerRequest with id " + CustomerRequestId + " not found");
    }
}
