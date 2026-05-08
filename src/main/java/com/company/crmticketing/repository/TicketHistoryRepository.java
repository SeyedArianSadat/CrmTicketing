package com.company.crmticketing.repository;


import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.TicketHistory;
import com.company.crmticketing.model.enums.RequestStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketHistoryRepository extends BaseEntityRepository<TicketHistory, Long> {
    Optional<TicketHistory> findByFieldChanged(String fieldChanged);

    Optional<TicketHistory> findByOldValue(String oldValue);


}
