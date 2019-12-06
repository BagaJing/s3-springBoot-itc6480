package com.awsdemo.demo.controllers;


import com.amazonaws.Response;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.awsdemo.demo.StreamLambdaHandler;
import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.services.amazonClient;
import com.awsdemo.demo.services.customerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
//@RequestMapping("/depoytest3/client")
@RequestMapping("/client")
public class clientLoginController {
    private String RELOGIN = "redirect:/depoytest3/client/login";
    private String REINDEX = "redirect:/depoytest3/client/index";
    private String REREGISTER = "redirect:/depoytest3/client/index";
    @Autowired
    private customerService customerService;
    @Autowired
    private amazonClient amazonClient;

    private StreamLambdaHandler handler = new StreamLambdaHandler();
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(){
        return  "register";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes attributes){
        Customer customer = customerService.checkCustomer(username,password);
        if (customer == null){
            //attributes.addFlashAttribute("message","username or password is not valid");
            return "redirect:/depoytest3/client/login";
        }
        customer.setPassword(null);
        //attributes.addFlashAttribute("customer",customer);
        return "redirect:/depoytest3/client/index/"+customer.getId();
    }
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("nickname") String nickname){
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setNickname(nickname);
        customer.setCreatedDate(new Date());
        customer.setActive(true);
        if (amazonClient.createCustomer(nickname)){
            Customer c = customerService.save(customer);
            if (c!=null){
               // attributes.addFlashAttribute("message","Register Successfully");
                return RELOGIN;
            }
           // attributes.addFlashAttribute("message","Register Failed");
            return REREGISTER;
        }
       // attributes.addFlashAttribute("message","Register Failed");
        return REREGISTER;
    }
    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes attributes) {
        session.removeAttribute("customer");
        attributes.addFlashAttribute("message","Successfully log out");
        return RELOGIN;
    }
}
