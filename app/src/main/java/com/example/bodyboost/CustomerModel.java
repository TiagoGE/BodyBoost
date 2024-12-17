package com.example.bodyboost;

public class CustomerModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int customerID;
    private String userGoal;
    private Float weight;
    private Float height;
    private int age;

    public CustomerModel(String firstName, String lastName, String email, String password, String userGoal, Float weight, Float height, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userGoal = userGoal;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }

    public CustomerModel(Integer id, String firstName, String lastName, String email, String password){
        this.customerID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserGoal() {
        return userGoal;
    }

    public void setUserGoal(String userGoal) {
        this.userGoal = userGoal;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }


    public Float getHeight() {
        return height;
    }


    public void setHeight(Float height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
