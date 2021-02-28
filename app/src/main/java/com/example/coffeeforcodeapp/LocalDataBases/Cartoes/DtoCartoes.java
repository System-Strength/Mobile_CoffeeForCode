package com.example.coffeeforcodeapp.LocalDataBases.Cartoes;

public class DtoCartoes {
    int id;
    String cpfproprietario, bandeira, numero, nomedotitular, validade, ccc;

    public DtoCartoes(int id, String cpfproprietario, String bandeira, String numero, String nomedotitular, String validade, String ccc) {
        this.id = id;
        this.cpfproprietario = cpfproprietario;
        this.bandeira = bandeira;
        this.numero = numero;
        this.nomedotitular = nomedotitular;
        this.validade = validade;
        this.ccc = ccc;
    }

    public DtoCartoes(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpfproprietario() {
        return cpfproprietario;
    }

    public void setCpfproprietario(String cpfproprietario) {
        this.cpfproprietario = cpfproprietario;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomedotitular() {
        return nomedotitular;
    }

    public void setNomedotitular(String nomedotitular) {
        this.nomedotitular = nomedotitular;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }
}
