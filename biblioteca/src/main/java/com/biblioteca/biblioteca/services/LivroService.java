package com.biblioteca.biblioteca.services;

import com.biblioteca.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.biblioteca.models.Editora;
import com.biblioteca.biblioteca.models.Livro;
import com.biblioteca.biblioteca.repositories.EditoraRepository;
import com.biblioteca.biblioteca.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EditoraRepository editoraRepository;

    public Livro criarLivro(LivroRequestDTO dto) throws Exception {
        // Validação (simples)
        if (dto.getTitulo() == null || dto.getIsbn() == null) {
            throw new Exception("Título e ISBN são obrigatórios.");
        }
        if (livroRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new Exception("Um livro com este ISBN já existe.");
        }
        Editora editora = editoraRepository.findById(dto.getIdEditora())
                .orElseThrow(() -> new Exception("Editora não encontrada."));

        // Criação da Entidade
        Livro novoLivro = new Livro();
        novoLivro.setTitulo(dto.getTitulo());
        novoLivro.setIsbn(dto.getIsbn());
        novoLivro.setAnoPublicacao(dto.getAnoPublicacao());
        novoLivro.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        novoLivro.setEditora(editora);
        
        // (Lógica para adicionar Autores e Categorias seria mais complexa)

        return livroRepository.save(novoLivro);
    }
    
    public void deletarLivro(Integer idLivro) throws Exception {
        if (!livroRepository.existsById(idLivro)) {
            throw new Exception("Livro não encontrado.");
        }
        // (Aqui seria necessário verificar se o livro tem empréstimos ativos)
        
        // (Remover associações N-M antes de deletar)
        // livroRepository.deleteFromLivroAutor(idLivro);
        // livroRepository.deleteFromLivroCategoria(idLivro);
        
        livroRepository.deleteById(idLivro);
    }
}