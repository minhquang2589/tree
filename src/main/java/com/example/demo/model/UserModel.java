package com.example.demo.model;

public class UserModel {
    private final int user_id;
    private String name = "";
    private String email = "";
    private String phone;
    private String gender = "";
    private String role = "";
    private final String birthday;
    private String image = "";
    private String address = "";
    private String password = "";


    public UserModel(int user_id, String name, String email, String phone, String gender, String role, String birthday, String image, String password, String address) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.role = role;
        this.birthday = birthday;
        this.image = image;
        this.password = password;
        this.address = address;
    }

    public int getId() {
        return user_id;
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
        return String.format("User{user_id=%d, name='%s', email='%s', phone='%s', gender='%s', role='%s', birthday='%s', image='%s', password='%s', address='%s'}",
                user_id, name, email, phone, gender, role, birthday, image, password, address);
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

    public void setAddress(String value) {
        this.address = value;
    }
}
