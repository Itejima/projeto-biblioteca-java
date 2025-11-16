package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name = "livros")
public class Livro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro") private Integer idLivro;
    @Column(name = "isbn", unique = true, nullable = false) private String isbn;
    @Column(name = "titulo", nullable = false) private String titulo;
    @Column(name = "ano_publicacao") private Integer anoPublicacao;
    @Column(name = "quantidade_estoque", nullable = false) private int quantidadeEstoque;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_editora") private Editora editora;
    @ManyToMany @JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "id_livro"), inverseJoinColumns = @JoinColumn(name = "id_autor")) private Set<Autor> autores;
    @ManyToMany @JoinTable(name = "livro_categoria", joinColumns = @JoinColumn(name = "id_livro"), inverseJoinColumns = @JoinColumn(name = "id_categoria")) private Set<Categoria> categorias;
    @OneToMany(mappedBy = "livro") private Set<Emprestimo> emprestimos;
    // Getters e Setters...
    public Integer getIdLivro() {
        return idLivro;
    }
    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }
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
    public Editora getEditora() {
        return editora;
    }
    public void setEditora(Editora editora) {
        this.editora = editora;
    }
    public Set<Autor> getAutores() {
        return autores;
    }
    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }
    public Set<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }
    public Set<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
    public void setEmprestimos(Set<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}