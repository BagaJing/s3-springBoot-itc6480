package com.awsdemo.demo.controllers;


import com.amazonaws.Response;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.awsdemo.demo.StreamLambdaHandler;
import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.services.amazonClient;
import com.awsdemo.demo.services.cognitoService;
import com.awsdemo.demo.services.customerService;
import com.awsdemo.demo.services.transactionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vo.userBasic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Random;

@Controller
//@RequestMapping("/depoytest3/client")
@RequestMapping("/client")
public class clientLoginController {
    private String RELOGIN = "redirect:/depoytest3/client/login";
    private String REINDEX = "redirect:/depoytest3/client/index";
    private String REREGISTER = "redirect:/depoytest3/client/index";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private transactionInterface transactionInterface;
    @GetMapping("/test")
    public String test(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session == null)
            return "test";
        else
            return "login";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(){
        return  "cognito-re";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password){
        cognitoService cognito = new cognitoService();
        userBasic userBasic = cognito.userLogin(username,password);
        if (userBasic!=null){
            logger.info("login succeed");
            logger.info(userBasic.toString());
            return "redirect:/depoytest3/client/index/"+userBasic.getId();
        }
        return "redirect:/depoytest3/client/login";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String nickname) throws Exception {
        cognitoService cognito = new cognitoService();
        if (cognito.findUserByEmail(email)!=null) {
            logger.info("Email "+email+" has been registered");
            return "redirect:/depoytest3/client/register";
        }
        try {
            userBasic userBasic = new userBasic();
            userBasic.setEmail(email);
            userBasic.setId(new Random().nextLong());
            userBasic.setUsername(username);
            userBasic.setNickname(nickname);
            transactionInterface.createUser(userBasic);
        }catch (Exception e){
            return e.getMessage();
        }
        return  "redirect:/depoytest3/client/login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes attributes) {
        session.removeAttribute("customer");
        attributes.addFlashAttribute("message","Successfully log out");
        return RELOGIN;
    }
}
