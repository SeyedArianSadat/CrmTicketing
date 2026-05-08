package com.company.crmticketing.repository;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.RequestType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRequestRepository extends BaseEntityRepository<CustomerRequest, Long> {


    Optional<CustomerRequest> findByTitle(String title);

    Optional<CustomerRequest> findByRequestType(RequestType requestType);

   // Optional<CustomerRequest> findByCustomerId(Long customerId);

}
