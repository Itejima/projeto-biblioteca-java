package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.sql.Date;
@Entity @Table(name = "emprestimos")
public class Emprestimo {
    @Id @Column(name = "id_emprestimo", length = 20) private String idEmprestimo;
    @Column(name = "data_emprestimo", nullable = false) private Date dataEmprestimo;
    @Column(name = "data_vencimento", nullable = false) private Date dataVencimento;
    @Column(name = "data_devolucao") private Date dataDevolucao;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_livro", nullable = false) private Livro livro;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_usuario", nullable = false) private Usuario usuario;
    // Getters e Setters...
    public String getIdEmprestimo() {
        return idEmprestimo;
    }
    public void setIdEmprestimo(String idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }
    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }
    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    public Date getDataVencimento() {
        return dataVencimento;
    }
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    public Date getDataDevolucao() {
        return dataDevolucao;
    }
    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    public Livro getLivro() {
        return livro;
    }
    public void setLivro(Livro livro) {
        this.livro = livro;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}