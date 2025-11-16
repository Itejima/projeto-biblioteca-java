package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name = "autores")
public class Autor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor") private Integer idAutor;
    @Column(name = "nome", nullable = false) private String nome;
    @ManyToMany(mappedBy = "autores") private Set<Livro> livros;
    // Getters e Setters...
    public Integer getIdAutor() {
        return idAutor;
    }
    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
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