package com.awsdemo.demo.domain;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Customer {
    @Id
    private Long id;
    private String nickname;
    private String email;
    @OneToMany(mappedBy = "customer")
    private List<Record> records;

    public Customer() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
