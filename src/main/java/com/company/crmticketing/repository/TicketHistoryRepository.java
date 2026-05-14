package com.company.crmticketing.repository;



import com.company.crmticketing.model.TicketHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketHistoryRepository extends BaseEntityRepository<TicketHistory, Long> {
    Optional<TicketHistory> findByFieldChanged(String fieldChanged);

    @Query("select t from ticketHistoryEntity t join fetch t.ticket where t.ticketHistoryId= :id")
    Optional<TicketHistory> findByIdWithTicket(@Param("id") Long id);

    @Query("select t from ticketHistoryEntity t join fetch t.ticket")
    List<TicketHistory> findAllWithTicket();


}
