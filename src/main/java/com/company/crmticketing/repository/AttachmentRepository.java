package com.company.crmticketing.repository;

import com.company.crmticketing.model.Attachment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface AttachmentRepository extends BaseEntityRepository<Attachment, Long> {

    Optional<Attachment> findByFileName(String fileName);

    @Query("select a from attachmentEntity a JOIN FETCH a.ticket where a.attachmentId= :id")
    Optional<Attachment> findByIdWithTicket(@Param("id") Long id);

    @Query("select a from attachmentEntity a JOIN FETCH a.ticket")
    List<Attachment> findAllWithTicket();


    @Query("select a from attachmentEntity a JOIN FETCH a.ticket t where t.ticketId = :TicketId")
    Optional<Attachment> findByTicketIdWithTicket(@Param("TicketId") Long TicketId);

}
