package com.agenciacronos.siteinstitucional.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Integrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "{campo.nomeIntegrante.obrigatorio}")
    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    @NotEmpty(message = "{campo.imgPerfil.obrigatorio}")
    private String imgPerfil;

    @Column(length = 255)
    private String funcao;

    @Column(length = 255)
    private String linkedin;

    @Column(length = 255)
    private String github;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyy")
    private LocalDate dataCadastro;

    public Integrante(String nome, String imgPerfil, String funcao, String linkedin, String github) {
        this.nome = nome;
        this.imgPerfil = imgPerfil;
        this.funcao = funcao;
        this.linkedin = linkedin;
        this.github = github;
    }

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }


}
