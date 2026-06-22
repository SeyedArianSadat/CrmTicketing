package com.company.crmticketing.repository;

import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends BaseEntityRepository<Ticket, Long> {
    Optional<Ticket> findByTitle(String title);

    List<Ticket> findByPriority(Priority priority);

    List<Ticket> findByRequestStatus(RequestStatus requestStatus);


    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN fetch  t.department
            LEFT JOIN fetch  t.agent
            LEFT JOIN fetch  t.customerRequest
            LEFT JOIN fetch  t.attachments
            where t.ticketId = :id""")
    Optional<Ticket> findByIdWithAllDetails(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN FETCH t.attachments
            where t.ticketId = :id""")
    Optional<Ticket> findByIdWithAttachments(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN FETCH t.messages
            where t.ticketId = :id""")
    Optional<Ticket> findByIdWithMessages(@Param("id") Long id);


    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN FETCH t.ticketHistories
            where t.ticketId = :id""")
    Optional<Ticket> findByIdWithTicketHistories(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN FETCH t.department
            LEFT JOIN FETCH t.agent""")
    List<Ticket> findAllWithDepartmentAndAgent();


    @Query("""
            SELECT DISTINCT t from ticketEntity t
            LEFT JOIN FETCH t.sla
            WHERE t.department.departmentId = :depId""")
    List<Ticket> findByDepartmentIdWithSla(@Param("depId") Long depId);

    boolean existsByTitle(String title);
}
