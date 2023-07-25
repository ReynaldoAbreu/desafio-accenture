package com.example.apicadastro.api.resource;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.api.dto.SupplierDTO;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.entity.Supplier;
import com.example.apicadastro.service.CompanyService;
import com.example.apicadastro.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fornecedor")
public class SupplierController {

    private final SupplierService service;
    private final ModelMapper modelMapper;

    public SupplierController(SupplierService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierDTO create(@RequestBody @Valid SupplierDTO dto){

        Supplier entity = modelMapper.map(dto, Supplier.class);
        entity = service.save(entity);

        return modelMapper.map(entity, SupplierDTO.class);

    }

    @GetMapping
    public SupplierDTO get(@PathVariable Long id){

        return service.getById(id)
                .map( supplier -> modelMapper.map(supplier, SupplierDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));


    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){

        Supplier supplier = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(supplier);

    }

    @PutMapping("{id}")
    public SupplierDTO update(@PathVariable Long id, SupplierDTO dto){

        return service.getById(id).map(supplier -> {
            supplier.setCep(dto.getCep());
            supplier.setNome(dto.getNome());
            supplier.setEmail(dto.getEmail());
            supplier = service.update(supplier);
            return modelMapper.map(supplier, SupplierDTO.class);
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }


}
