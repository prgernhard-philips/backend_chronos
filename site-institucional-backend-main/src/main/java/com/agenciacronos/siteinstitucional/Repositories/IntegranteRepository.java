package com.agenciacronos.siteinstitucional.Repositories;

import com.agenciacronos.siteinstitucional.models.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntegranteRepository extends JpaRepository<Integrante, Long> {

    List<Integrante> findByNomeContainingIgnoreCase (String nome);
}
