package com.example.firebaseauthentication;

public class ModelCalss {
    String Name,phone,Email;

    public ModelCalss(String name, String phone, String email) {
        Name = name;
        this.phone = phone;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
