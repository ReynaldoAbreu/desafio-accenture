package com.example.apicadastro.service.impl;

import com.example.apicadastro.exception.AddressNotFoundException;
import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.exception.ResourceNotFoundException;
import com.example.apicadastro.model.AddressExternApi;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.repository.CompanyRepository;
import com.example.apicadastro.model.repository.SupplierRepository;
import com.example.apicadastro.service.CompanyService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository repository;
    private  SupplierRepository supplierRepository;

    public CompanyServiceImpl(CompanyRepository repository) {
        this.repository = repository;

    }

    @Override
    public Company save(Company company) {

        try {

                company.setEstado(searchState(company.getCep()));
            System.out.println(company.getEstado());

        }catch (Exception ex){
            throw new AddressNotFoundException("Cep informado é invalido");
        }

        if (repository.existsByCnpj(company.getCnpj())){

            throw new BusinessException("CNPJ já cadstrado.");
        }

        return repository.save(company);
    }

    @Override
        public Company saveSupplier(Long idCompany, Long idSupplier) {

        String estado = "GO";

        SupplierServiceImpl supplierService = new SupplierServiceImpl(supplierRepository);

        Company company = repository.findById(idCompany)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + idCompany));
        if (Objects.equals(company.getEstado(), estado)) {

            if (supplierService.getEgeSupplier(idSupplier)) {
                throw new BusinessException("Não deve cadastrar fornecedor menor de 18 para esta empresa");
            }
        }

        company.getSuppliers().add(supplierService.getById(idSupplier).get());

        return company;
    }

    @Override
    public Optional<Company> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Company company) {

        if (company == null || company.getId() == null){

            throw new IllegalArgumentException("O id não deve ser nulo");
        }

        repository.delete(company);

    }

    @Override
    public Company update(Company company) {

        if (company == null || company.getId() == null){

                throw new IllegalArgumentException("O id não encontrado");

        }

        return repository.save(company);
    }

    @Override
    public List<Company> getAll() {
        return repository.findAll();
    }

    //acabei utilizando o via cep para busca de endereço por cep, tive dificuldade em entender a api do cep lá.
    // Este serviço trata apenas de cep de localidade nacional, infelizmente entendi encima da hora que se tratava de Panama país.

    public String searchState(String companyCep) throws Exception {

        URL url = new URL("https://viacep.com.br/ws/"+companyCep+"/json/");
        System.out.println(url);
        URLConnection connection = url.openConnection();
        try (InputStream is = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String cep = "";
            StringBuilder jsonCep = new StringBuilder();
            while ((cep = reader.readLine()) != null) {
                jsonCep.append(cep);
            }

            System.out.println(jsonCep);

            AddressExternApi addressExternApi = new Gson().fromJson(jsonCep.toString(), AddressExternApi.class);

            return addressExternApi.getUf();
        }
    }
}
