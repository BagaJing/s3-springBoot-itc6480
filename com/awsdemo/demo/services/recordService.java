package com.awsdemo.demo.services;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.domain.Record;

import java.util.List;

public interface recordService{
    Record save(Record record);
    List<Record> listByCustomer(Customer customer);
}
