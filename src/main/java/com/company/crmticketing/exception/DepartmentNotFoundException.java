package com.company.crmticketing.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long DepartmentId) {
        super("Department with id " + DepartmentId + " not found");
    }
}
