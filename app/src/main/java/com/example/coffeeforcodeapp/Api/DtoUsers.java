package com.example.coffeeforcodeapp.Api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class DtoUsers {
    int id_user;
    String email, nm_user, rg_user, phone_user, password, partner, message;

    public DtoUsers(String email, String nm_user, String rg_user, String password){
        this.email = email;
        this.nm_user = nm_user;
        this.rg_user = rg_user;
        this.password = password;

    }

    public DtoUsers(int id_user, String email, String nm_user, String rg_user, String phone_user, String password, String partner) {
        this.id_user = id_user;
        this.email = email;
        this.nm_user = nm_user;
        this.rg_user = rg_user;
        this.phone_user = phone_user;
        this.password = password;
    }

    public DtoUsers(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNm_user() {
        return nm_user;
    }

    public void setNm_user(String nm_user) {
        this.nm_user = nm_user;
    }

    public String getRg_user() {
        return rg_user;
    }

    public void setRg_user(String rg_user) {
        this.rg_user = rg_user;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Email: " + email + "\nSenha: " + password;
    }
}
