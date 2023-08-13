package com.example.apicadastro.service;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.model.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company save(Company company);

    Company saveSupplier(Long idCompany, Long idSupplier);

    Optional<Company> getById(Long id);

    void delete(Company company);

    Company update(Company company);

    List<Company> getAll();
}
