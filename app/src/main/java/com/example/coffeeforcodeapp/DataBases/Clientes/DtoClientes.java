package com.example.coffeeforcodeapp.DataBases.Clientes;

public class DtoClientes {
    int id;
    String nomecliente, cpfcliente, emailcliente, celularcliente, enderecocliente, complementocliente, parceiro, adm, senhacliente;

    public DtoClientes(int id, String nomecliente, String cpfcliente, String emailcliente, String celularcliente, String enderecocliente, String complementocliente, String parceiro, String adm, String senhacliente) {
        this.id = id;
        this.nomecliente = nomecliente;
        this.cpfcliente = cpfcliente;
        this.emailcliente = emailcliente;
        this.celularcliente = celularcliente;
        this.enderecocliente = enderecocliente;
        this.complementocliente = complementocliente;
        this.parceiro = parceiro;
        this.adm = adm;
        this.senhacliente = senhacliente;
    }

    public DtoClientes(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomecliente() {
        return nomecliente;
    }

    public void setNomecliente(String nomecliente) {
        this.nomecliente = nomecliente;
    }

    public String getCpfcliente() {
        return cpfcliente;
    }

    public void setCpfcliente(String cpfcliente) {
        this.cpfcliente = cpfcliente;
    }

    public String getEmailcliente() {
        return emailcliente;
    }

    public void setEmailcliente(String emailcliente) {
        this.emailcliente = emailcliente;
    }

    public String getCelularcliente() {
        return celularcliente;
    }

    public void setCelularcliente(String celularcliente) {
        this.celularcliente = celularcliente;
    }

    public String getEnderecocliente() {
        return enderecocliente;
    }

    public void setEnderecocliente(String enderecocliente) {
        this.enderecocliente = enderecocliente;
    }

    public String getComplementocliente() {
        return complementocliente;
    }

    public void setComplementocliente(String complementocliente) {
        this.complementocliente = complementocliente;
    }

    public String getParceiro() {
        return parceiro;
    }

    public void setParceiro(String parceiro) {
        this.parceiro = parceiro;
    }

    public String getAdm() {
        return adm;
    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public String getSenhacliente() {
        return senhacliente;
    }

    public void setSenhacliente(String senhacliente) {
        this.senhacliente = senhacliente;
    }
}
