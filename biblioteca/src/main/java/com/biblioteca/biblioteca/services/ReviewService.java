package com.biblioteca.biblioteca.services;
import com.biblioteca.biblioteca.dto.ReviewRequest;
import com.biblioteca.biblioteca.models.Usuario;
import com.biblioteca.biblioteca.models.mongo.Review;
import com.biblioteca.biblioteca.repositories.UsuarioRepository;
import com.biblioteca.biblioteca.repositories.mongo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
@Service
public class ReviewService {
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Review> getReviewsByBook(Integer idLivro) {
        return reviewRepository.findByIdLivro(idLivro);
    }

    public Review createReview(ReviewRequest dto) throws Exception {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new Exception("Usuário SQL não encontrado."));
        Review review = new Review();
        review.setIdLivro(dto.getIdLivro());
        review.setIdUsuario(dto.getIdUsuario());
        review.setNota(dto.getNota());
        review.setComentario(dto.getComentario());
        review.setData(Instant.now());
        review.setNomeUsuario(usuario.getNome());
        return reviewRepository.save(review);
    }
}