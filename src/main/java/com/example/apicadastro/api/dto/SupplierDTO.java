package com.example.apicadastro.api.dto;

import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDTO {

    private Long id;

    @NotEmpty
    private Tipo tipo;

    @NotEmpty
    private String nome;

    @NotEmpty
    private String email;

    private String cnpj;

    private String cpf;

    @NotEmpty
    private String cep;

    private String rg;

    private LocalDate dataNascimento;

    private List<Company> companies;

}
