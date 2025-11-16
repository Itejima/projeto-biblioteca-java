package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {}