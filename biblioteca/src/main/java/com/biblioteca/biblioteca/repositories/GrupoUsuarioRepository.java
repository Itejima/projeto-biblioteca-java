package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.GrupoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Integer> {}