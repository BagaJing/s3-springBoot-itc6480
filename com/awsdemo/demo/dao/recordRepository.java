package com.awsdemo.demo.dao;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface recordRepository extends JpaRepository<Record,Long> {
    List<Record> findAllByCustomer(Customer customer);
}
