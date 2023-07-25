package com.example.apicadastro.service.impl;

import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.exception.ResourceNotFoundException;
import com.example.apicadastro.model.entity.Supplier;
import com.example.apicadastro.model.enums.Tipo;
import com.example.apicadastro.model.repository.SupplierRepository;
import com.example.apicadastro.service.SupplierService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;
    public SupplierServiceImpl(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supplier save(Supplier supplier) {

        if (supplier.getTipo().equals(Tipo.PESSOA_FISICA)){
            if (supplier.getRg() == null || supplier.getDataNascimento() == null){
                throw new IllegalArgumentException("Rg e Data de Nascimento não deve ser nul");
            }
        }
        return repository.save(supplier);
    }

    @Override
    public Optional<Supplier> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Supplier supplier) {

        if (supplier == null || supplier.getId() == null){

            throw new IllegalArgumentException("O id não deve ser nulo");
        }

        repository.delete(supplier);

    }

    @Override
    public Supplier update(Supplier supplier) {
        if (supplier == null || supplier.getId() == null){

            throw new IllegalArgumentException("O id não encontrado");

        }

        return repository.save(supplier);
    }

    public Boolean getEgeSupplier(Long idSupplier){

        LocalDate dataAtual = LocalDate.now();


        Supplier supplier = repository.findById(idSupplier)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com o ID: " + idSupplier));

        LocalDate dateSupplier = supplier.getDataNascimento();


        return dateSupplier.isAfter(dataAtual.minusYears(18));
    }

}
