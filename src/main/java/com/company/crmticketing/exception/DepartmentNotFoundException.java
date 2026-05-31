package com.company.crmticketing.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long departmentId) {
        super("Department with id " + departmentId + " not found");
    }
}
