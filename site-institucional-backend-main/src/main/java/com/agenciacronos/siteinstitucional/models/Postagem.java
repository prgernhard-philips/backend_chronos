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
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    @NotEmpty(message = "{campo.tituloBlog.obrigatorio}")
    private String titulo;

    @Column(nullable = false)
    @NotEmpty(message = "{campo.postagem.obrigatorio}")
    private String postagem;

    @Column(nullable = false)
    @NotEmpty(message = "{campo.imagemPostagem.obrigatorio}")
    private String imagem;


    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyy")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }

    public Postagem(String titulo, String postagem, String imagem) {
        this.titulo = titulo;
        this.postagem = postagem;
        this.imagem = imagem;
    }
}
