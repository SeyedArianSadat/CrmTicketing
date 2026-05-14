package com.company.crmticketing.repository;

import com.company.crmticketing.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MessageRepository extends BaseEntityRepository<Message, Long> {
    Optional<Message> findByContent(String content);

    @Query("select m from messageEntity m JOIN FETCH m.senderUser where m.messageId= :id")
    Optional<Message> findByIdWithUser(@Param("id") Long id);

    @Query("select m from messageEntity m JOIN FETCH m.ticket where m.messageId= :id")
    Optional<Message> findByIdWithTicket(@Param("id") Long id);

    @Query("""
            SELECT m FROM messageEntity m
            left join fetch m.senderUser
            left join fetch m.ticket
            where m.messageId = :id""")
    Optional<Message> findByIdWithTicketAndSender(@Param("id") Long id);


}
