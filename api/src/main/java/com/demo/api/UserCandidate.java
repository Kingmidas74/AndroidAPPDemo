package com.demo.api;

import com.google.gson.annotations.SerializedName;

final class UserCandidate {
    @SerializedName("EMail")
    private String EMail;
    @SerializedName("Password")
    private String Password;
    @SerializedName("Repassword")
    private String Repassword;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Surname")
    private String Surname;

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRepassword() {
        return Repassword;
    }

    public void setRepassword(String repassword) {
        Repassword = repassword;
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
}
