package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
    List<Livro> findByTituloContaining(String titulo);
    Optional<Livro> findByIsbn(String isbn);
}