package com.example.demo.entity;


import jakarta.persistence.*;

@Entity

public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String password;
    private String first_name;
 private String email;
public users() {}


    public void setName(String first_name){
        this.first_name=first_name;
    }

    public String getName(){
       return first_name;
    }


    public void setPassword(String password){
        this.password=password;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getEmail(){
        return email;
    }


}