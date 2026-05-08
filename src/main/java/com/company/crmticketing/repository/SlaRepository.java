package com.company.crmticketing.repository;

import com.company.crmticketing.model.Sla;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlaRepository extends BaseEntityRepository<Sla, Long> {
    Optional<Sla> findByPriorityLevel(Priority priority);
}
