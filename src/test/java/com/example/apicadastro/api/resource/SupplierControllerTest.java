package com.example.apicadastro.api.resource;

import com.example.apicadastro.api.dto.CompanyDTO;
import com.example.apicadastro.api.dto.SupplierDTO;
import com.example.apicadastro.model.entity.Company;
import com.example.apicadastro.model.entity.Supplier;
import com.example.apicadastro.service.CompanyService;
import com.example.apicadastro.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Subclass;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = SupplierController.class)
@AutoConfigureMockMvc
public class SupplierControllerTest {

    static String  COMPANY_API = "/api/fornecedor";

    @Autowired
    MockMvc mvc;

    @MockBean
    SupplierService service;

    @Test
    @DisplayName("Deve criar uma empresa com sucesso")
    public void createCompanyTest() throws Exception {

        SupplierDTO dto = SupplierDTO.builder()
                .cep("000-000")
                .email("fulano@fulano")
                .nome("Fulano")
                .build();

        BDDMockito.given(service.save(Mockito.any(Supplier.class)))
                .willReturn(Supplier.builder()
                        .id(1L)
                        .cep("000-000")
                        .email("fulano@fulano")
                        .nome("Fulano")
                .build());

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
                .andExpect(jsonPath("nome").value(dto.getNome()))
                .andExpect(jsonPath("cep").value(dto.getCep()));

    }


}
