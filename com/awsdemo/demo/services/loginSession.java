package com.awsdemo.demo.services;

public class loginSession {
    private Long id;
    private String access_token;

    public loginSession(Long id, String access_token) {
        this.id = id;
        this.access_token = access_token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
