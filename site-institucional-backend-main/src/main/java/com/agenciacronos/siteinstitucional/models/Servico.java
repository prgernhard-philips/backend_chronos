package com.agenciacronos.siteinstitucional.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 150)
    @NotEmpty(message = "{campo.titulo.obrigatorio}")
    private String titulo;

    @Column(nullable = false, length = 255)
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @Column(nullable = false)
    @NotEmpty(message = "{campo.img.obrigatorio}")
    private String imgUrl;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyy")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }

    public Servico(String titulo, String descricao, String imgUrl) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imgUrl = imgUrl;
    }
}
