package com.company.crmticketing.repository;

import com.company.crmticketing.model.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface DepartmentRepository extends BaseEntityRepository<Department, Long> {

    Optional<Department> findByDepartmentName(String departmentName);

    @Query("select d from departmentEntity d join fetch d.supportAgents  where d.departmentId= :id")
    Optional<Department> findByIdWithSupportAgent(@Param("id") Long id);

    @Query("select d from departmentEntity d join fetch d.tickets where d.departmentId= :id")
    Optional<Department> findByIdWithTickets(@Param("id") Long id);

    @Query("""
            SELECT d FROM departmentEntity d
            left join fetch d.supportAgents
            left join fetch d.tickets
            where d.departmentId = :id""")
    Optional<Department> findByIdWithAgentsAndTickets(@Param("id") Long id);


}
