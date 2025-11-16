package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name = "grupos_usuarios")
public class GrupoUsuario {
    @Id @Column(name = "id_grupo") private Integer idGrupo;
    @Column(name = "nome_grupo", nullable = false, unique = true) private String nomeGrupo;
    @OneToMany(mappedBy = "grupoUsuario") private Set<Usuario> usuarios;
    // Getters e Setters...
    public Integer getIdGrupo() {
        return idGrupo;
    }
    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }
    public String getNomeGrupo() {
        return nomeGrupo;
    }
    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}