package com.biblioteca.biblioteca.dto;
public class UsuarioDTO {
    private String idUsuario;
    private String nome;
    private String email;
    private GrupoUsuarioDTO grupoUsuario;
    // Getters e Setters...
    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
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
    public GrupoUsuarioDTO getGrupoUsuario() {
        return grupoUsuario;
    }
    public void setGrupoUsuario(GrupoUsuarioDTO grupoUsuario) {
        this.grupoUsuario = grupoUsuario;
    }
}