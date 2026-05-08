package com.company.crmticketing.repository;

import com.company.crmticketing.model.Message;
import com.company.crmticketing.model.enums.RequestType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends BaseEntityRepository<Message, Long> {
    Optional<Message> findByContent(String content);

}
