package com.company.crmticketing.repository;

import com.company.crmticketing.model.SupportAgent;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportAgentRepository extends BaseEntityRepository<SupportAgent, Long> {

    Optional<SupportAgent> findByAgentName(String agentName);

}
