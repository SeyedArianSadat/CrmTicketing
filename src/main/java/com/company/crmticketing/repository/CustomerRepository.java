package com.company.crmticketing.repository;

import com.company.crmticketing.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseEntityRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(Long customerId);

    Optional<Customer> findByCustomerName(String customerName);

    @Query("select c from customerEntity c join fetch c.user where c.customerId= :id")
    Optional<Customer> findByCustomerIdWithUser(@Param("id") Long id);

    @Query("select c from customerEntity c join fetch c.customerRequests where c.customerId= :id")
    Optional<Customer> findByCustomerIdWithCustomerRequest(@Param("id") Long id);

    @Query("select c from customerEntity c join fetch c.customerRequests")
    List<Customer> findAllCustomersWithCustomerRequests();

}
