package com.company.crmticketing.repository;

import com.company.crmticketing.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseEntityRepository<Customer, Long> {

    Optional<Customer> findByCustomerName(String customerName);

    @Query("select c from customerEntity  c where c.deleted=false ")
    List<Customer> findAllActiveCustomers();

}
