package com.example.doctrocareapp.model;

public class Doctor {
    private String name;
    private String address;
    private String tel;
    private String email;
    private String specialist;

    public Doctor(){
        //needed for firebase
    }

    public Doctor(String name, String address, String tel, String email, String specialist) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.specialist = specialist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }
}
