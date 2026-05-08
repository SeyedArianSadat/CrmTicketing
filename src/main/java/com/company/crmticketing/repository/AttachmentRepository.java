package com.company.crmticketing.repository;

import com.company.crmticketing.model.Attachment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.Set;

@Repository
public interface AttachmentRepository extends BaseEntityRepository<Attachment, Long> {

    Optional<Attachment> findByFileName(String fileName);
}
