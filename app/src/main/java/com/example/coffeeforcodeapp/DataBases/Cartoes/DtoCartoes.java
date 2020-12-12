package com.example.coffeeforcodeapp.DataBases.Cartoes;

public class DtoCartoes {
    int id;
    String cpfproprietario, numrero, nomerotitular, validade, ccc;

    public DtoCartoes(int id, String cpfproprietario, String numrero, String nomerotitular, String validade, String ccc) {
        this.id = id;
        this.cpfproprietario = cpfproprietario;
        this.numrero = numrero;
        this.nomerotitular = nomerotitular;
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

    public String getNumrero() {
        return numrero;
    }

    public void setNumrero(String numrero) {
        this.numrero = numrero;
    }

    public String getNomerotitular() {
        return nomerotitular;
    }

    public void setNomerotitular(String nomerotitular) {
        this.nomerotitular = nomerotitular;
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
