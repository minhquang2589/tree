package com.example.demo.model;

public class UserModel {
    private final int id;
    private String name = "";
    private String email = "";
    private String phone;
    private String address = "";
    private String gender = "";
    private String role = "";
    private final String birthday;
    private String image = "";
    private String password = "";


    public UserModel(int id, String name, String email, String phone, String address, String gender, String role, String birthday, String image, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.role = role;
        this.birthday = birthday;
        this.image = image;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', phone='%s', address='%s', gender='%s', role='%s', birthday='%s', image='%s', password='%s'}",
                id, name, email, phone, address, gender, role, birthday, image, password);
    }

    public void setName(String text) {
        this.name = text;
    }

    public void setEmail(String text) {
        this.email = text;
    }

    public void setPhone(String text) {
        this.phone = text;
    }

    public void setAddress(String text) {
        this.address = text;
    }

    public void setGender(String value) {
        this.gender = value;
    }

    public void setRole(String value) {
        this.role = value;
    }

    public void setImage(String value) {
        this.image = value;
    }

    public void setPasword(String value) {
        this.password = value;
    }
}
