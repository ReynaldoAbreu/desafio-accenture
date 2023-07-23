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

    private static CompanyDTO createNewCompany() {
        return CompanyDTO.builder()
                .nomeFantasia("minha Empresa")
                .cnpj("123456")
                .cep("000-000")
                .build();
    }

}
