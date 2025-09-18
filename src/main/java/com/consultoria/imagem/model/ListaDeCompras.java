package com.consultoria.imagem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListaDeCompras {
    private int id;
    private Cliente cliente;
    private String nome; // Novo campo para o nome da lista
    private LocalDate dataCriacao;
    private List<Peca> pecas;

    // Construtor completo
    public ListaDeCompras(int id, String nome, Cliente cliente, LocalDate dataCriacao, List<Peca> pecas) {
        this.id = id;
        this.nome = nome;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.pecas = pecas;
    }

    // Construtor para nova lista (sem ID e com nome)
    public ListaDeCompras(String nome, Cliente cliente, LocalDate dataCriacao) {
        this.nome = nome;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.pecas = new ArrayList<>();
    }

    // Construtor para nova lista (sem ID, nome e peças)
    public ListaDeCompras(String nome, Cliente cliente, LocalDate dataCriacao, List<Peca> pecas) {
        this.nome = nome;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.pecas = pecas;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        return nome + " (" + dataCriacao.toString() + ")";
    }
}
