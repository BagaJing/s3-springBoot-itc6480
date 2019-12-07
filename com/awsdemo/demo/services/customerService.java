package com.awsdemo.demo.services;

import com.awsdemo.demo.domain.Customer;

import java.util.Optional;

public interface customerService {
    Customer findCustomerById(Long id);
    Customer save(Customer customer);
}
