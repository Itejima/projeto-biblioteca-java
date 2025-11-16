package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.dto.CatalogoDTO;
import com.biblioteca.biblioteca.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ViewCatalogoRepository extends JpaRepository<Livro, Integer> {
    @Query(value = "SELECT id_livro, titulo, autores, categorias, quantidade_estoque " +
                   "FROM vw_livros_disponiveis_catalogo",
           nativeQuery = true)
    List<CatalogoDTO> findCatalogoDisponivel();
}