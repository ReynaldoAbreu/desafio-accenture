package com.example.apicadastro.model.entity;

import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome_fantasia")
    private String nomeFantasia;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Column()
    private String estado;

    @Column(nullable = false)
    private String cep;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Supplier> suppliers;
}
