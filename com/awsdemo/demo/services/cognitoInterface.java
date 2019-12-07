package com.awsdemo.demo.services;

import com.awsdemo.demo.domain.Customer;
import vo.userBasic;

public interface cognitoInterface {
    void createNewUser(userBasic user);
}
