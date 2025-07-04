package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTestCreate {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateVet() throws Exception {

        String VET_FIRSTNAME = "Eduardo";
        String LASTNAME = "Vera";

        VetDTO newVetDTO = new VetDTO();
        newVetDTO.setFirstname(VET_FIRSTNAME);
        newVetDTO.setLastname(LASTNAME);

        this.mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", is(VET_FIRSTNAME)))
                .andExpect(jsonPath("$.lastname", is(LASTNAME)));

    }


    @Test
    public void testDeleteVet() throws Exception {

        String VET_FIRSTNAME = "Eduardo";
        String LASTNAME = "Bullon";

        VetDTO newVetTO = new VetDTO();
        newVetTO.setFirstname(VET_FIRSTNAME);
        newVetTO.setLastname(LASTNAME);

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/vets/" + id ))
                /*.andDo(print())*/
                .andExpect(status().isOk());
    }


}
