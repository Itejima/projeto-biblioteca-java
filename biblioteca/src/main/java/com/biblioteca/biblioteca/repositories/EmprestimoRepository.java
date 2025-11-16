package com.biblioteca.biblioteca.repositories;
import com.biblioteca.biblioteca.models.Emprestimo;
import com.biblioteca.biblioteca.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, String> {
    List<Emprestimo> findByUsuario(Usuario usuario);
    @Procedure(name = "sp_registrar_devolucao")
    void registrarDevolucao(@Param("p_id_emprestimo") String idEmprestimo);
    @Query(value = "SELECT fnc_gerar_id_emprestimo()", nativeQuery = true)
    String getNewEmprestimoId();
    @Query(value = "SELECT fnc_verificar_pendencias_usuario(:p_id_usuario)", nativeQuery = true)
    boolean verificarPendencias(@Param("p_id_usuario") String idUsuario);
}