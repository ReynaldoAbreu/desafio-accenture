package com.example.apicadastro.api.resource;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.exception.BusinessException;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    static String  COMPANY_API = "/api/empresa";

    @Autowired
    MockMvc mvc;

    @MockBean
    CompanyService service;

    @Test
    @DisplayName("Deve criar uma empresa com sucesso")
    public void createCompanyTest() throws Exception {

        CompanyDTO dto = createNewCompany();

        Company companySaved = Company.builder()
                .id(1L)
                .nomeFantasia("minha Empresa")
                .cnpj("123456")
                .cep("000-000")
                .build();

        BDDMockito.given(service.save(Mockito.any(Company.class))).willReturn(companySaved);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(COMPANY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("cnpj").value(dto.getCnpj()))
                .andExpect(jsonPath("nomeFantasia").value(dto.getNomeFantasia()))
                .andExpect(jsonPath("cep").value(dto.getCep()));

    }

    @Test
    @DisplayName("Deve lançar erro quando não tiver dados suficietes para criar uma empresa")
    public void createInvalidCompanyTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new CompanyDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(COMPANY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadstrar uma empresa com cnpj já existente")
    public void createCompanyWithDuplicateCnpj() throws Exception {

        CompanyDTO dto = createNewCompany();
        String json = new ObjectMapper().writeValueAsString(dto);
        String messageError = "CNPJ já cadstrado.";

        BDDMockito.given(service.save(Mockito.any(Company.class)))
                .willThrow(new BusinessException(messageError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(COMPANY_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(messageError));

    }

    @Test
    @DisplayName("Deve obter informações de uma empresa ")
    public void getCompanyDetail() throws Exception {

        Long id = 1L;

        Company company = Company.builder()
                .id(id)
                .nomeFantasia(createNewCompany().getNomeFantasia())
                .cnpj(createNewCompany().getCnpj())
                .cep(createNewCompany().getCep())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(company));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(COMPANY_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("cnpj").value(createNewCompany().getCnpj()))
                .andExpect(jsonPath("nomeFantasia").value(createNewCompany().getNomeFantasia()))
                .andExpect(jsonPath("cep").value(createNewCompany().getCep()));

    }

    @Test
    @DisplayName("Deve retornar resourse not found quando não encontrar uma empresa ")
    public void companyNotFound() throws Exception {


        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(COMPANY_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }
    @Test
    @DisplayName("Deve deletar uma empresa ")
    public void deleteCompanyTest() throws Exception {


        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Company.builder().id(1L).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(COMPANY_API.concat("/" + 1));

        mvc.perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve retornar resource not found quando não encontrar uma empresa para deletar")
    public void deleteNoExistCompanyTest() throws Exception {


        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(COMPANY_API.concat("/" + 1));

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve atualizar uma empresa")
    public void updateCompanyTest() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewCompany());

        Company updatingCompany = Company.builder()
                .id(id).nomeFantasia("algum nome").cnpj("123456").cep("000-111").build();

        BDDMockito.given( service.getById(id) ).willReturn( Optional.of(updatingCompany) );

        Company updatedCompany = Company.builder()
                .id(id)
                .cnpj("123456")
                .nomeFantasia("minha Empresa")
                .cep("000-000")
                .build();
        BDDMockito.given(service.update(updatingCompany)).willReturn(updatedCompany);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(COMPANY_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("cnpj").value(createNewCompany().getCnpj()))
                .andExpect(jsonPath("nomeFantasia").value(createNewCompany().getNomeFantasia()))
                .andExpect(jsonPath("cep").value(createNewCompany().getCep()));

    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar uma empresa inexistente")
    public void updateNoExistentCompanyTest() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewCompany());
        BDDMockito.given( service.getById(id))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(COMPANY_API.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }






    private static CompanyDTO createNewCompany() {
        return CompanyDTO.builder()
                .nomeFantasia("minha Empresa")
                .cnpj("123456")
                .cep("000-000")
                .build();
    }

}
