package com.awsdemo.demo.controllers;



import com.awsdemo.demo.services.cognitoService;
import com.awsdemo.demo.services.customerService;
import com.awsdemo.demo.services.loginSession;
import com.awsdemo.demo.services.transactionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vo.userBasic;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;

@Controller
@RequestMapping("/serverless/client") //test
//@RequestMapping("/client")
public class clientLoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private transactionInterface transactionInterface;
    @Autowired
    private customerService customerService;
    @GetMapping("/test")
    public String test(HttpSession session){
        if (session == null)
            session = new HttpSession() {
                @Override
                public long getCreationTime() {
                    return 0;
                }

                @Override
                public String getId() {
                    return null;
                }

                @Override
                public long getLastAccessedTime() {
                    return 0;
                }

                @Override
                public ServletContext getServletContext() {
                    return null;
                }

                @Override
                public void setMaxInactiveInterval(int i) {

                }

                @Override
                public int getMaxInactiveInterval() {
                    return 0;
                }

                @Override
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                @Override
                public Object getAttribute(String s) {
                    return null;
                }

                @Override
                public Object getValue(String s) {
                    return null;
                }

                @Override
                public Enumeration<String> getAttributeNames() {
                    return null;
                }

                @Override
                public String[] getValueNames() {
                    return new String[0];
                }

                @Override
                public void setAttribute(String s, Object o) {

                }

                @Override
                public void putValue(String s, Object o) {

                }

                @Override
                public void removeAttribute(String s) {

                }

                @Override
                public void removeValue(String s) {

                }

                @Override
                public void invalidate() {

                }

                @Override
                public boolean isNew() {
                    return false;
                }
            };
        if (session == null)
            return "test";
        else
            return "login";
    }
    @GetMapping("/login")
    public String loginPage(Model model,@ModelAttribute("message") String message){
        model.addAttribute("message",message);
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(Model model,@ModelAttribute("message") String message){
        model.addAttribute("message",message);
        return  "cognito-re";
    }
    @GetMapping("/reset")
    public String resetPage(Model model,@ModelAttribute("message") String message){
        model.addAttribute("message",message);
        return "reset";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       RedirectAttributes attributes) throws IOException {
        cognitoService cognito = new cognitoService();
        loginSession loginSession = cognito.userLogin(username,password);
        if (loginSession!=null){
            logger.info("login succeed");
            //attributes.addAttribute("userId",userBasic.getId());
            if (loginSession.getAccess_token()!=null){
                attributes.addAttribute("token",loginSession.getAccess_token());
                return "redirect:/serverless/client/index/"+loginSession.getId();
            }else{
                attributes.addAttribute("message","please change the temp password");
                return "redirect:/serverless/client/login";
            }

        }
        attributes.addAttribute("message","the username or the password is not valid");
        return"redirect:/serverless/client/login";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String credit,
                           RedirectAttributes attributes) throws Exception {
        if (!customerService.isNicknameExist(username)){
            logger.info("the username "+username+" has been used");
            attributes.addAttribute("message","Sorry,the username "+username+" has already been used.");
            return "redirect:/serverless/client/register";
        }
        cognitoService cognito = new cognitoService();
        if (cognito.findUserByEmail(email)!=null) {
            logger.info("Email "+email+" has been registered");
            attributes.addAttribute("message","Sorry,the email "+email+" has already been used.");
            return "redirect:/serverless/client/register";
        }
        try {
            userBasic userBasic = new userBasic();
            userBasic.setEmail(email);
            userBasic.setId(new Random().nextLong());
            userBasic.setUsername(username);
            userBasic.setNickname(username);
            userBasic.setCreditNumber(credit);
            transactionInterface.createUser(userBasic);
        }catch (Exception e){
            return e.getMessage();
        }
        attributes.addAttribute("message","A temp password will be sent to your email");
        return  "redirect:/serverless/client/login";
    }
    @PostMapping("/reset")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                RedirectAttributes attributes){
        cognitoService cognito = new cognitoService();
        boolean rslt = cognito.changeFromTempPassword(username,oldPassword,newPassword);
        if (rslt){
            attributes.addAttribute("message","Successfully Reset!");
            return "redirect:/serverless/client/login";
        } else{
            attributes.addAttribute("message","Reset Failed, please check your username and password");
            return "redirect:/serverless/client/reset";
        }
    }
}
