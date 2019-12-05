package com.awsdemo.demo.controllers;

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
@RequestMapping("/dev")
public class loginController {
    @Autowired
    private customerService customerService;
    @Autowired
    private amazonClient amazonClient;
    @GetMapping("/login")
    public String loginPage(){
        return "login-dev";
    }
    @GetMapping("/register")
    public String registerPage(){
        return  "register-dev";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        Customer customer = customerService.checkCustomer(username,password);
        if (customer == null){
            attributes.addFlashAttribute("message","username or password is not valid");
            return "redirect:/dev";
        }
        customer.setPassword(null);
        session.setAttribute("customer",customer);
        return "redirect:/dev/index";
    }
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("nickname") String nickname,
                           RedirectAttributes attributes){
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setNickname(nickname);
        customer.setCreatedDate(new Date());
        customer.setActive(true);
        if (amazonClient.createCustomer(nickname)){
            Customer c = customerService.save(customer);
            if (c!=null){
                attributes.addFlashAttribute("message","Register Successfully");
                return "redirect:/dev";
            }
            attributes.addFlashAttribute("message","Register Failed");
            return "redirect:/dev/register";
        }
        attributes.addFlashAttribute("message","Register Failed");
        return "redirect:/dev/register";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes attributes) {
        session.removeAttribute("customer");
        attributes.addFlashAttribute("message","Successfully log out");
        return "redirect:/dev/login";
    }
}
