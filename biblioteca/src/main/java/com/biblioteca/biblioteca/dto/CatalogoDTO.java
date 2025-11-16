package com.biblioteca.biblioteca.dto;
public interface CatalogoDTO {
    Integer getId_livro();
    String getTitulo();
    String getAutores();
    String getCategorias();
    Integer getQuantidade_estoque();
}