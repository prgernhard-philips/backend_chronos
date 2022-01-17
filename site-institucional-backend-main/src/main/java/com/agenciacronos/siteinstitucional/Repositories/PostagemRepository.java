package com.agenciacronos.siteinstitucional.Repositories;

import com.agenciacronos.siteinstitucional.models.Integrante;
import com.agenciacronos.siteinstitucional.models.Postagem;
import com.agenciacronos.siteinstitucional.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    List<Postagem> findByTituloContainingIgnoreCase (String titulo);
}
