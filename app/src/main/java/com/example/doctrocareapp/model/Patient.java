package com.example.doctrocareapp.model;

public class Patient {
    private String name;
    private String address;
    private String tel;
    private String email;
    private String dateOfBirth;


    public Patient(){
        //needed for firebase
    }

    public Patient(String name, String address, String tel, String email, String dateOfBirth) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
