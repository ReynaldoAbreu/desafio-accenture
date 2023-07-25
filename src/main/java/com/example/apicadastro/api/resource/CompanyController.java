package com.example.apicadastro.api.resource;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.api.exception.ApiErrors;
import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.entity.Supplier;
import com.example.apicadastro.service.CompanyService;
import com.example.apicadastro.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresa")
public class CompanyController {

    private final CompanyService service;
    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    public CompanyController(CompanyService service, SupplierService supplierService, ModelMapper modelMapper) {
        this.service = service;
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO create(@RequestBody @Valid CompanyDTO dto){

        Company entity = modelMapper.map(dto, Company.class);
        entity = service.save(entity);

        return modelMapper.map(entity, CompanyDTO.class);

    }
    @PostMapping("{companyId}/supplier/{supplierId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO saveSupplier(@PathVariable Long idCompany, @PathVariable Long idSupplier){



        Company entity = service.saveSupplier(idCompany, idSupplier);

        return modelMapper.map(entity, CompanyDTO.class);

    }

    @GetMapping ("{id}")
    public CompanyDTO get(@PathVariable Long id) throws Throwable {

        return service.getById(id)
                .map( company -> modelMapper.map(company, CompanyDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){

        Company company = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(company);

    }

    @PutMapping("{id}")
    public CompanyDTO update(@PathVariable Long id, @RequestBody CompanyDTO dto){

        return service.getById(id).map(company -> {
            company.setCep(dto.getCep());
            company.setNomeFantasia(dto.getNomeFantasia());
            company = service.update(company);
            return modelMapper.map(company, CompanyDTO.class);
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){

        return new ApiErrors(ex);

    }

}
