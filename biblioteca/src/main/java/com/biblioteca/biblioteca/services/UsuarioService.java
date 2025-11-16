package com.biblioteca.biblioteca.services;
import com.biblioteca.biblioteca.dto.GrupoUsuarioDTO;
import com.biblioteca.biblioteca.dto.UsuarioDTO;
import com.biblioteca.biblioteca.models.GrupoUsuario;
import com.biblioteca.biblioteca.models.Usuario;
import com.biblioteca.biblioteca.repositories.GrupoUsuarioRepository;
import com.biblioteca.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UsuarioService {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private GrupoUsuarioRepository grupoUsuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public UsuarioDTO registrarUsuario(String nome, String email, String senhaPlana, Integer idGrupo) throws Exception {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email já cadastrado.");
        }
        GrupoUsuario grupo = grupoUsuarioRepository.findById(idGrupo).orElseThrow(() -> new Exception("Grupo de usuário com ID " + idGrupo + " não encontrado."));
        String novoId = usuarioRepository.getNewUsuarioId();
        String hashSenha = passwordEncoder.encode(senhaPlana);
        Usuario novoUsuario = new Usuario();
        novoUsuario.setIdUsuario(novoId);
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setHashSenha(hashSenha);
        novoUsuario.setGrupoUsuario(grupo);
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return toDTO(usuarioSalvo);
    }

    public UsuarioDTO login(String email, String senhaPlana) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new Exception("Email ou senha inválidos."));
        if (passwordEncoder.matches(senhaPlana, usuario.getHashSenha())) {
            return toDTO(usuario);
        } else {
            throw new Exception("Email ou senha inválidos.");
        }
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        GrupoUsuarioDTO grupoDTO = new GrupoUsuarioDTO();
        grupoDTO.setIdGrupo(usuario.getGrupoUsuario().getIdGrupo());
        grupoDTO.setNomeGrupo(usuario.getGrupoUsuario().getNomeGrupo());
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setGrupoUsuario(grupoDTO);
        return dto;
    }
}