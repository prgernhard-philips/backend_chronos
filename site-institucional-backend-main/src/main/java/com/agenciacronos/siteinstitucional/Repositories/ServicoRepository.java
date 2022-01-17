package com.agenciacronos.siteinstitucional.Repositories;

import com.agenciacronos.siteinstitucional.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByTituloContainingIgnoreCase (String titulo);

}
