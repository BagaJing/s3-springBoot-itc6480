package com.awsdemo.demo.services;

import com.awsdemo.demo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.userBasic;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class transcations implements transactionInterface {
@Autowired
private amazonClient amazonClient;
@Autowired
private customerService customerService;
    @Override
    @Transactional
    public void createUser(userBasic userBasic) {
        try {
            cognitoInterface cogito = new cognitoService();
            cogito.createNewUser(userBasic);
            amazonClient.createCustomer(userBasic.getNickname());
            Customer c = new Customer();
            c.setId(userBasic.getId());
            c.setNickname(userBasic.getNickname());
            c.setEmail(userBasic.getEmail());
            customerService.save(c);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
