package com.example.apicadastro.service.impl;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.repository.CompanyRepository;
import com.example.apicadastro.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository repository;

    public CompanyServiceImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Company save(Company company) {

        if (repository.existsByCnpj(company.getCnpj())){
            throw new BusinessException("CNPJ já cadstrado.");
        }
        return repository.save(company);
    }

    @Override
    public Optional<Company> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Company update(Company company) {
        return null;
    }
}
