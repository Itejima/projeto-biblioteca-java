package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name = "editoras")
public class Editora {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editora") private Integer idEditora;
    @Column(name = "nome", nullable = false) private String nome;
    @OneToMany(mappedBy = "editora") private Set<Livro> livros;
    // Getters e Setters...
    public Integer getIdEditora() {
        return idEditora;
    }
    public void setIdEditora(Integer idEditora) {
        this.idEditora = idEditora;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Set<Livro> getLivros() {
        return livros;
    }
    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }
}