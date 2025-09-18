package com.consultoria.imagem.model;

public class Cliente {
    private int id;
    private String nome;
    private String contato;
    private String observacoes;

    public Cliente(int id, String nome, String contato, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.observacoes = observacoes;
    }

    public Cliente(String nome, String contato, String observacoes) {
        this.nome = nome;
        this.contato = contato;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return nome;
    }
}


