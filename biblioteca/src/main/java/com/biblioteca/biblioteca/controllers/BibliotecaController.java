package com.biblioteca.biblioteca.controllers;

// DTOs
import com.biblioteca.biblioteca.dto.*;
// Model SQL
import com.biblioteca.biblioteca.models.Livro;
// Model NoSQL
import com.biblioteca.biblioteca.models.mongo.Review;
// Services
import com.biblioteca.biblioteca.services.*;

// Spring Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite acesso do seu index.html
public class BibliotecaController {

    // --- Injeção de Dependências ---
    @Autowired private CatalogoService catalogoService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private EmprestimoService emprestimoService;
    @Autowired private ReviewService reviewService;
    @Autowired private LivroService livroService; // (NOVO)

    // --- Endpoints de Usuário (SQL) ---

    @GetMapping("/catalogo")
    public ResponseEntity<List<CatalogoDTO>> getCatalogo() {
        List<CatalogoDTO> catalogo = catalogoService.getCatalogoDisponivel();
        return ResponseEntity.ok(catalogo);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest request) {
        try {
            UsuarioDTO novoUsuario = usuarioService.registrarUsuario(
                request.getNome(), request.getEmail(), request.getSenha(),
                request.getIdGrupo() != null ? request.getIdGrupo() : 3
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UsuarioDTO usuario = usuarioService.login(request.getEmail(), request.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // --- Endpoints de Empréstimo (SQL) ---

    @PostMapping("/emprestimos")
    public ResponseEntity<?> realizarEmprestimo(@RequestBody EmprestimoRequest request) {
        try {
            EmprestimoDTO novoEmprestimo = emprestimoService.realizarEmprestimo(
                request.getIdUsuario(), request.getIdLivro()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novoEmprestimo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/devolucoes/{idEmprestimo}")
    public ResponseEntity<?> registrarDevolucao(@PathVariable String idEmprestimo) {
        try {
            emprestimoService.registrarDevolucao(idEmprestimo);
            return ResponseEntity.ok("Devolução registrada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- (NOVOS) Endpoints de Admin/Livros (SQL) ---

    @PostMapping("/livros")
    public ResponseEntity<?> criarLivro(@RequestBody LivroRequestDTO request) {
        try {
            Livro novoLivro = livroService.criarLivro(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/livros/{id}")
    public ResponseEntity<?> deletarLivro(@PathVariable Integer id) {
        try {
            livroService.deletarLivro(id);
            return ResponseEntity.ok("Livro deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoints NoSQL (Reviews) ---

    @GetMapping("/reviews/{idLivro}")
    public ResponseEntity<List<Review>> getReviewsDoLivro(@PathVariable Integer idLivro) {
        List<Review> reviews = reviewService.getReviewsByBook(idLivro);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> adicionarReview(@RequestBody ReviewRequest request) {
        try {
            Review novoReview = reviewService.createReview(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoReview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}