package com.consultoria.imagem.model;

public class Peca {
    private int id;
    private String nome;
    private String descricao;
    private String fotoPath;
    private Loja loja;
    private double valor;
    private String observacoes;

    public Peca(int id, String nome, String descricao, String fotoPath, Loja loja, double valor, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fotoPath = fotoPath;
        this.loja = loja;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public Peca(String nome, String descricao, String fotoPath, Loja loja, double valor, String observacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.fotoPath = fotoPath;
        this.loja = loja;
        this.valor = valor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return nome + " (R$ " + String.format("%.2f", valor) + ")";
    }
}


