package com.mycompany.myapp.domain;

import javax.persistence.*;

@Entity
public class Passport {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "passport_number")
    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Customer customer;

    public Passport() {
    }

    public Passport(String passportNumber) {
        this.passportNumber = passportNumber;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Passport{" +
            "id=" + id +
            ", passportNumber='" + passportNumber + '\'' +
            ", customer=" + customer +
            '}';
    }
}
