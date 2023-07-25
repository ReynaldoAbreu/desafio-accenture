package com.example.apicadastro.service;

import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.entity.Supplier;

import java.util.Optional;

public interface SupplierService {

    Supplier save(Supplier supplier);

    Optional<Supplier> getById(Long id);

    void delete(Supplier supplier);

    Supplier update(Supplier supplier);
}
