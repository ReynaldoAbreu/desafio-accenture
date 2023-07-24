package com.example.apicadastro.service;

import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.repository.CompanyRepository;
import com.example.apicadastro.service.impl.CompanyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedInputStream;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CompanyServiceTest {

    CompanyService service;
    @MockBean
    CompanyRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new CompanyServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar uma empresa")
    public void saveCompanyTest(){

        Company company = createValidCompany();
        Mockito.when(repository.existsByCnpj(Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(company)).thenReturn(Company.builder()
                .id(1L)
                .nomeFantasia("minha empresa")
                .cnpj("123456")
                .cep("000-000")
                .build());

        Company savedCompany = service.save(company);

        assertThat(savedCompany.getId()).isNotNull();
        assertThat(savedCompany.getNomeFantasia()).isEqualTo("minha empresa");
        assertThat(savedCompany.getCnpj()).isEqualTo("123456");
        assertThat(savedCompany.getCep()).isEqualTo("000-000");

    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar uma empresa com Cbpj já cadastrado")
    public void shouldNotSaveCompanyWithDuplicateCNPJ(){
        Company company = createValidCompany();
        Mockito.when(repository.existsByCnpj(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(company));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("CNPJ já cadstrado.");

        Mockito.verify(repository, Mockito.never()).save(company);

    }

    @Test
    @DisplayName("Deve Obter uma empresa por Id.")
    public void getByIdTest(){

        Long id = 1L;
        Company company = createValidCompany();
        company.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(company));

        Optional<Company> foundCompany = service.getById(id);

        assertThat(foundCompany.isPresent()).isTrue();
        assertThat(foundCompany.get().getId()).isEqualTo(id);
        assertThat(foundCompany.get().getCnpj()).isEqualTo(company.getCnpj());
        assertThat(foundCompany.get().getCep()).isEqualTo(company.getCep());
        assertThat(foundCompany.get().getNomeFantasia()).isEqualTo(company.getNomeFantasia());


    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar empresa por id não existente na base")
    public void companyNotFoundById(){

        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Company> company = service.getById(id);

        assertThat(company.isPresent()).isFalse();

    }

    private static Company createValidCompany() {
        return Company.builder()
                .nomeFantasia("minha empresa")
                .cnpj("123456")
                .cep("000-000")
                .build();
    }

}
