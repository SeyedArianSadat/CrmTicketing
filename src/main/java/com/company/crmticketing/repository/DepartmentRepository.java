package com.company.crmticketing.repository;

import com.company.crmticketing.model.Department;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends BaseEntityRepository<Department, Long> {

    Optional<Department> findByDepartmentName(String departmentName);

}
