package com.example.coffeeforcodeapp.LocalDataBases.Parceiro;


public class DtoParceiro {
    int id;
    String nomecliente, cpfcliente, emailcliente, numerocartao, termosaceito;
    String data_ativacao, data_cancelamento;
    String ccccartaocfc;

    public DtoParceiro(int id, String nomecliente, String cpfcliente, String emailcliente, String numerocartao, String termosaceito, String data_ativacao, String data_cancelamento, String ccccartaocfc) {
        this.id = id;
        this.nomecliente = nomecliente;
        this.cpfcliente = cpfcliente;
        this.emailcliente = emailcliente;
        this.numerocartao = numerocartao;
        this.termosaceito = termosaceito;
        this.data_ativacao = data_ativacao;
        this.data_cancelamento = data_cancelamento;
        this.ccccartaocfc = ccccartaocfc;
    }

    public DtoParceiro(){}

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

    public String getNumerocartao() {
        return numerocartao;
    }

    public void setNumerocartao(String numerocartao) {
        this.numerocartao = numerocartao;
    }

    public String getTermosaceito() {
        return termosaceito;
    }

    public void setTermosaceito(String termosaceito) {
        this.termosaceito = termosaceito;
    }

    public String getData_ativacao() {
        return data_ativacao;
    }

    public void setData_ativacao(String data_ativacao) {
        this.data_ativacao = data_ativacao;
    }

    public String getData_cancelamento() {
        return data_cancelamento;
    }

    public void setData_cancelamento(String data_cancelamento) {
        this.data_cancelamento = data_cancelamento;
    }

    public String getCcccartaocfc() {
        return ccccartaocfc;
    }

    public void setCcccartaocfc(String ccccartaocfc) {
        this.ccccartaocfc = ccccartaocfc;
    }
}
