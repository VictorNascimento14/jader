package com.consultoria.imagem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListaDeCompras {
    private int id;
    private Cliente cliente;
    private LocalDate dataCriacao;
    private List<Peca> pecas;

    public ListaDeCompras(int id, Cliente cliente, LocalDate dataCriacao, List<Peca> pecas) {
        this.id = id;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.pecas = pecas;
    }

    public ListaDeCompras(Cliente cliente, LocalDate dataCriacao) {
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.pecas = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<Peca> getPecas() {
        return pecas;
    }

    public void setPecas(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public void addPeca(Peca peca) {
        this.pecas.add(peca);
    }

    public void removePeca(Peca peca) {
        this.pecas.remove(peca);
    }

    @Override
    public String toString() {
        return "Lista para " + cliente.getNome() + " em " + dataCriacao.toString();
    }
}


