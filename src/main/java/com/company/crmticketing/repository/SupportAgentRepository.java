package com.company.crmticketing.repository;

import com.company.crmticketing.model.SupportAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SupportAgentRepository extends BaseEntityRepository<SupportAgent, Long> {

    Optional<SupportAgent> findByAgentName(String agentName);

    @Query("select s from supportAgentEntity s join fetch s.user where s.agentId= :id")
    Optional<SupportAgent> findByIdWithUser(@Param("id") Long id);

    @Query("select s from supportAgentEntity s join fetch s.department where s.agentId= :id")
    Optional<SupportAgent> findByIdWithDepartment(@Param("id") Long id);

    @Query("select s from supportAgentEntity s join fetch s.assignedTickets where s.agentId= :id")
    Optional<SupportAgent> findByIdWithAssignedTickets(@Param("id") Long id);

    @Query("""
           select s from supportAgentEntity s
           LEFT JOIN fetch s.department
           LEFT JOIN fetch s.user
           where s.agentId= :id""")
    List<SupportAgent> findAllByIdAgentWithDepartmentAndUser(@Param("id") Long id);

    @Query("""
           select s from supportAgentEntity s
           LEFT JOIN fetch s.assignedTickets
           where s.agentId= :id""")
    List<SupportAgent> findAllByAssignedTickets(@Param("id") Long id);
}
