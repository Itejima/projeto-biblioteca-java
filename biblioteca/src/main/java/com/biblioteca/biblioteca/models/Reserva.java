package com.biblioteca.biblioteca.models;
import jakarta.persistence.*;
import java.sql.Timestamp;
@Entity @Table(name = "reservas")
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva") private Integer idReserva;
    @Column(name = "data_reserva", insertable = false, updatable = false) private Timestamp dataReserva;
    @Column(name = "status_reserva", nullable = false) @Enumerated(EnumType.STRING) private StatusReserva statusReserva;
    public enum StatusReserva { Ativa, Cancelada, Atendida }
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_livro", nullable = false) private Livro livro;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_usuario", nullable = false) private Usuario usuario;
    public Reserva() { this.statusReserva = StatusReserva.Ativa; }
    // Getters e Setters...
    public Integer getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }
    public Timestamp getDataReserva() {
        return dataReserva;
    }
    public void setDataReserva(Timestamp dataReserva) {
        this.dataReserva = dataReserva;
    }
    public StatusReserva getStatusReserva() {
        return statusReserva;
    }
    public void setStatusReserva(StatusReserva statusReserva) {
        this.statusReserva = statusReserva;
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