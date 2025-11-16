package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EditoraRepository extends JpaRepository<Editora, Integer> {}