package com.example.apicadastro.model.repository;

import com.example.apicadastro.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCnpj(String cnpj);
}
