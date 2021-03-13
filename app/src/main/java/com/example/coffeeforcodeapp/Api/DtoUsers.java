package com.example.coffeeforcodeapp.Api;

public class DtoUsers {
    int id_user, partner;
    String email, nm_user, cpf_user, phone_user, address_user,complement, img_user, password, partner_Startdate, message;



    public DtoUsers(String address_user, String complement){
        this.address_user = address_user;
        this.complement = complement;

    }

    public DtoUsers(String email, String nm_user, String cpf_user, String password){
        this.email = email;
        this.nm_user = nm_user;
        this.cpf_user = cpf_user;
        this.password = password;

    }

    public DtoUsers(String nm_user, String cpf_user, String phone_user, String address_user, String complement){
        this.nm_user = nm_user;
        this.cpf_user = cpf_user;
        this.phone_user = phone_user;
        this.address_user = address_user;
        this.complement = complement;
    }

    public DtoUsers(int id_user, String email, String nm_user, String cpf_user, String phone_user, String password, int partner) {
        this.id_user = id_user;
        this.email = email;
        this.nm_user = nm_user;
        this.cpf_user = cpf_user;
        this.phone_user = phone_user;
        this.password = password;
    }
    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
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

    public String getCpf_user() {
        return cpf_user;
    }

    public void setCpf_user(String cpf_user) {
        this.cpf_user = cpf_user;
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

    public int getPartner() {
        return partner;
    }

    public void setPartner(int partner) {
        this.partner = partner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getPartner_Startdate() {
        return partner_Startdate;
    }

    public void setPartner_Startdate(String partner_Startdate) {
        this.partner_Startdate = partner_Startdate;
    }

    @Override
    public String toString() {
        return id_user + "\n" + email + "\n" + "\n" + cpf_user + "\n" +  nm_user + "\n" + password;
    }
}
