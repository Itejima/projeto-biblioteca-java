package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name = "categorias")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria") private Integer idCategoria;
    @Column(name = "nome_categoria", nullable = false, unique = true) private String nomeCategoria;
    @ManyToMany(mappedBy = "categorias") private Set<Livro> livros;
    // Getters e Setters...
    public Integer getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getNomeCategoria() {
        return nomeCategoria;
    }
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    public Set<Livro> getLivros() {
        return livros;
    }
    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }
}