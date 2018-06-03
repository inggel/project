/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.math.BigInteger;

/**
 *
 * @author LaraMondini
 */
public class Registro {
    private Long dataCriacao;
    private BigInteger chave;
    private String comando;
    private String valor;

    public Registro(Long dataCriacao, String comando, BigInteger chave, String valor) {
        this.dataCriacao = dataCriacao;
        this.chave = chave;
        this.valor = valor;
        this.comando = comando;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }
    
    
    
    public Long getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Long dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigInteger getChave() {
        return chave;
    }

    public void setChave(BigInteger chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
