package com.company.crmticketing.repository;

import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends BaseEntityRepository<Ticket, Long> {
    Optional<Ticket> findByTitle(String title);

    Optional<Ticket> findByPriority(Priority priority);

    Optional<Ticket> findByRequestStatus(RequestStatus requestStatus);

}
