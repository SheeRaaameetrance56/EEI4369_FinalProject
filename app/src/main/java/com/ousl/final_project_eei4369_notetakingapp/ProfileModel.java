package com.ousl.final_project_eei4369_notetakingapp;

public class ProfileModel {
    public int id;
    public String name;
    public String email;
    public String password;
    public boolean isRemember;

    // constructors
    public ProfileModel() {

    }

    public ProfileModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isRemember = isRemember;
    }

    public ProfileModel(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isRemember = isRemember;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean getIsRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }
}
