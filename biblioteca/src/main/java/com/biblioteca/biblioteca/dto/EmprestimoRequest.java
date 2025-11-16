// Arquivo: EmprestimoRequest.java
package com.biblioteca.biblioteca.dto;
public class EmprestimoRequest {
    private String idUsuario;
    private Integer idLivro;
    // Getters e Setters...
    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public Integer getIdLivro() {
        return idLivro;
    }
    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }
}