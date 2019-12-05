package com.awsdemo.demo.services;

import com.awsdemo.demo.dao.recordRepository;
import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.domain.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class recordServiceImpl implements recordService {
    @Autowired
    private recordRepository recordRepository;
    @Override
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public List<Record> listByCustomer(Customer customer) {
        return recordRepository.findAllByCustomer(customer);
    }
}
