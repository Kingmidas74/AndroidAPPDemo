package com.demo.api;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.ranges.RangeException;

/**
 * Created by midas on 18.02.18.
 */

public class User extends Entity  {
    @SerializedName("Name")
    public String Name;
    @SerializedName("Surname")
    public String Surname;
    @SerializedName("EMail")
    public String Email;
    @SerializedName("Balance")
    public Double Balance;
    @SerializedName("Hash")
    public String Hash;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        if(id<2) throw new IllegalArgumentException("id");
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        Balance = balance;
    }

    public String getHash() {return Hash;}

    public void setHash(String hash) {Hash = hash;}
}
