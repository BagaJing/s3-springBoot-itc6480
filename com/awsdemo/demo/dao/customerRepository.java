package com.awsdemo.demo.dao;

import com.awsdemo.demo.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface customerRepository extends JpaRepository<Customer,Long> {
    @Query("select c from Customer c where c.id=?1")
    Customer findOne(Long id);
}
