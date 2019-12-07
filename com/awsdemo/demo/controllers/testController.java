package com.awsdemo.demo.controllers;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.services.cognitoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vo.userBasic;

import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/test")
public class testController {
    @GetMapping("/cognitore")
    public String registerShow(){
        return  "cognito-re";
    }
    @PostMapping("/cognitore")
    public String newUser(@RequestParam String email,
                          @RequestParam String username,
                          @RequestParam String nickname){
        try {
            cognitoService cognito = new cognitoService();
            userBasic user = new userBasic();;
            user.setNickname(nickname);
            user.setEmail(email);
            user.setId(new Random().nextLong());
            user.setUsername(username);
            cognito.createNewUser(user);
        }catch (Exception e){
            return e.getMessage();
        }
        return  "redirect:/test/cognitore";
    }
}
