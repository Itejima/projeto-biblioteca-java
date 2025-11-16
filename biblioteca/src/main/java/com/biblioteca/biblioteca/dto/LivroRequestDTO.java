package com.biblioteca.biblioteca.dto;
public class LivroRequestDTO {
    private String isbn;
    private String titulo;
    private Integer idEditora;
    private Integer anoPublicacao;
    private int quantidadeEstoque;
    // Getters e Setters...
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getIdEditora() {
        return idEditora;
    }
    public void setIdEditora(Integer idEditora) {
        this.idEditora = idEditora;
    }
    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}