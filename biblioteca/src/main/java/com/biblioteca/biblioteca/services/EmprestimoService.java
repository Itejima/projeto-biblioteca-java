package com.biblioteca.biblioteca.services;
import com.biblioteca.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.biblioteca.models.*;
import com.biblioteca.biblioteca.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;
@Service
public class EmprestimoService {
    @Autowired private EmprestimoRepository emprestimoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private LivroRepository livroRepository;

    @Transactional
    public EmprestimoDTO realizarEmprestimo(String idUsuario, Integer idLivro) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new Exception("Usuário não encontrado."));
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new Exception("Livro não encontrado."));
        if (emprestimoRepository.verificarPendencias(usuario.getIdUsuario())) {
            throw new Exception("Usuário possui empréstimos atrasados.");
        }
        if (livro.getQuantidadeEstoque() <= 0) {
            throw new Exception("Livro sem estoque disponível.");
        }
        String novoIdEmprestimo = emprestimoRepository.getNewEmprestimoId();
        LocalDate hoje = LocalDate.now();
        LocalDate dataVencimento = hoje.plusDays(7); 
        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setIdEmprestimo(novoIdEmprestimo);
        novoEmprestimo.setUsuario(usuario);
        novoEmprestimo.setLivro(livro);
        novoEmprestimo.setDataEmprestimo(Date.valueOf(hoje));
        novoEmprestimo.setDataVencimento(Date.valueOf(dataVencimento));
        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);
        return toDTO(emprestimoSalvo);
    }

    @Transactional
    public void registrarDevolucao(String idEmprestimo) throws Exception {
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new Exception("Empréstimo não encontrado."));
        if (emprestimo.getDataDevolucao() != null) {
            throw new Exception("Este livro já foi devolvido.");
        }
        emprestimoRepository.registrarDevolucao(idEmprestimo);
    }
    
    private EmprestimoDTO toDTO(Emprestimo emprestimo) {
        EmprestimoDTO dto = new EmprestimoDTO();
        dto.setIdEmprestimo(emprestimo.getIdEmprestimo());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataVencimento(emprestimo.getDataVencimento());
        dto.setTituloLivro(emprestimo.getLivro().getTitulo()); 
        dto.setNomeUsuario(emprestimo.getUsuario().getNome());
        return dto;
    }
}