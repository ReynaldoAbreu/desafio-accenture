package com.example.apicadastro.service;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.model.entity.Company;

import java.util.Optional;

public interface CompanyService {
    Company save(Company company);

    Optional<Company> getById(Long id);

    void delete(Long id);

    Company update(Company company);
}
