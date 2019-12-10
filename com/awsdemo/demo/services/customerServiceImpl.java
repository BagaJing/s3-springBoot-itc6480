package com.awsdemo.demo.services;

import com.awsdemo.demo.dao.customerRepository;
import com.awsdemo.demo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class customerServiceImpl implements customerService {
    @Autowired
    private customerRepository customerRepository;


    @Override
    public Customer findCustomerById(Long id) {
        return customerRepository.findOne(id);
        //return customerRepository.f;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public boolean isNicknameExist(String nickname) {
        return customerRepository.findByNickname(nickname) == null;
    }
}
