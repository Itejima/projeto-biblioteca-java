package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
@Entity @Table(name = "usuarios")
public class Usuario {
    @Id @Column(name = "id_usuario", length = 20) private String idUsuario;
    @Column(name = "nome", nullable = false) private String nome;
    @Column(name = "email", nullable = false, unique = true) private String email;
    @Column(name = "hash_senha", nullable = false) private String hashSenha;
    @Column(name = "data_cadastro", insertable = false, updatable = false) private Timestamp dataCadastro;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_grupo", nullable = false) private GrupoUsuario grupoUsuario;
    @OneToMany(mappedBy = "usuario") private Set<Emprestimo> emprestimos;
    @OneToMany(mappedBy = "usuario") private Set<Reserva> reservas;
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
    public String getHashSenha() {
        return hashSenha;
    }
    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }
    public Timestamp getDataCadastro() {
        return dataCadastro;
    }
    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    public GrupoUsuario getGrupoUsuario() {
        return grupoUsuario;
    }
    public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
        this.grupoUsuario = grupoUsuario;
    }
    public Set<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
    public void setEmprestimos(Set<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
    public Set<Reserva> getReservas() {
        return reservas;
    }
    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }
}