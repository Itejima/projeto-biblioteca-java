package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    @Query(value = "SELECT fnc_gerar_id_usuario()", nativeQuery = true)
    String getNewUsuarioId();
}