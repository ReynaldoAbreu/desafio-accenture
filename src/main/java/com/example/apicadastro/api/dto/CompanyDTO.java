package com.example.apicadastro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {


    private Long id;
    @NotEmpty
    private String nomeFantasia;
    @NotEmpty
    private String cnpj;
    @NotEmpty
    private String cep;

}
