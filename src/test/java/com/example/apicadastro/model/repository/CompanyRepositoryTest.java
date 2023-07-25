package com.example.apicadastro.model.repository;

import com.example.apicadastro.model.entity.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CompanyRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir uma empresa na base com cnpj informado")
    public void returnTrueWhenCnpjExist(){

        String cnpj = "123456";

        Company company = createNewCompany(cnpj);

        entityManager.persist(company);

        boolean exists = repository.existsByCnpj(cnpj);

        assertThat(exists).isTrue();
    }



    @Test
    @DisplayName("Deve retornar falso quando não existir uma empresa na base com cnpj informado")
    public void returnFalseWhenCnpjNotExist(){

        String cnpj = "123456";

        boolean exists = repository.existsByCnpj(cnpj);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter uma empresa pelo id")
    public void findByIdTest(){

        Company company = createNewCompany("123456");
        entityManager.persist(company);

        Optional<Company> foundCompany = repository.findById(company.getId());

        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getId()).isEqualTo(company.getId());

    }

    private static Company createNewCompany(String cnpj) {
        return Company.builder()
                .nomeFantasia("minha empresa")
                .cnpj(cnpj)
                .cep("000-000")
                .build();
    }
}
