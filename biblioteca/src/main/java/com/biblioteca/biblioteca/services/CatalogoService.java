package com.biblioteca.biblioteca.services;
import com.biblioteca.biblioteca.dto.CatalogoDTO;
import com.biblioteca.biblioteca.repositories.ViewCatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CatalogoService {
    @Autowired private ViewCatalogoRepository viewCatalogoRepository;
    public List<CatalogoDTO> getCatalogoDisponivel() {
        return viewCatalogoRepository.findCatalogoDisponivel();
    }
}