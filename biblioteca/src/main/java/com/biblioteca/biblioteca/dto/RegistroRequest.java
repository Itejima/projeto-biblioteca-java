package com.biblioteca.biblioteca.dto;
public class RegistroRequest {
    private String nome;
    private String email;
    private String senha;
    private Integer idGrupo;
    // Getters e Setters...
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Integer getIdGrupo() {
        return idGrupo;
    }
    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }
}