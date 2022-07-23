package com.example.doctrocareapp.model;

public class User {
    private String name;
    private String birthday;
    private String tel;
    private String email;
    private String type;

    public User(){
        //need firebase
    }

    public User(String name, String birthday, String tel, String email, String type) {
        this.name = name;
        this.birthday = birthday;
        this.tel = tel;
        this.email = email;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
}
