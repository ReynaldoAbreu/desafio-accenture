package com.example.apicadastro.model.entity;

import com.example.apicadastro.model.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "cadastro")
    @Enumerated(EnumType.STRING)
    @NotBlank
    private Tipo tipo;

    @Column(nullable = false)
    private String nome;

    private String email;

    @Column(length = 14)
    private String cnpj;

    @Column(length = 11)
    private String cpf;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = true)
    private String rg;

    @Column(name = "data_de_nascimento", nullable = true)
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;



}
