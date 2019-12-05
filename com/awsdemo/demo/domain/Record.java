package com.awsdemo.demo.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    private String ordernum;
    private int number;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @ManyToOne
    private Customer customer;

    public Record() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getNumber() { return number; }

    public void setNumber(int number) { this.number = number; }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", createdDate=" + createdDate +
                ", customer=" + customer +
                '}';
    }
}
