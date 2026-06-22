package com.company.crmticketing.repository;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.RequestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface CustomerRequestRepository extends BaseEntityRepository<CustomerRequest, Long> {


    Optional<CustomerRequest> findByTitle(String title);

    Optional<CustomerRequest> findByRequestType(RequestType requestType);

    @Query("select c from customerRequestEntity c join fetch c.ticket where c.ticket.ticketId= :id")
    Optional<CustomerRequest> findCustomerRequestByTicket(@Param("id") Long id);

    @Query("select distinct c from customerRequestEntity c join fetch  c.messages m where m.messageId= :messageId")
    Optional<CustomerRequest> findCustomerRequestByMessages(@Param("messageId") Long messageId);


    @Query("""
            select c from customerRequestEntity c
            left join fetch c.ticket
            left join fetch c.messages
            left join fetch c.customer
            where c.requestId = :id""")
    Optional<CustomerRequest> findCustomerRequestByIdWithAllDetails(@Param("id") Long id);


}
