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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

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


    @Test
    public void testFindAllVets() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    @Test
    public void testFindVetOK() throws Exception {
        int VET_ID = 1;
        String FIRST_NAME = "James";
        String LAST_NAME = "Carter";

        this.mockMvc.perform(get("/vets/" + VET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(VET_ID)))
                .andExpect(jsonPath("$.firstname", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", is(LAST_NAME)));
    }

    @Test
    public void testFindVetKO() throws Exception {
        int NON_EXISTENT_ID = 666;

        this.mockMvc.perform(get("/vets/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVet() throws Exception {


        String FIRSTNAME = "David";
        String LASTNAME = "Johnson";

        String UP_FIRSTNAME = "David Updated";
        String UP_LASTNAME = "Johnson Updated";

        VetDTO newVetDTO = new VetDTO();
        newVetDTO.setFirstname(FIRSTNAME);
        newVetDTO.setLastname(LASTNAME);

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        VetDTO upVetDTO = new VetDTO();
        upVetDTO.setId(id);
        upVetDTO.setFirstname(UP_FIRSTNAME);
        upVetDTO.setLastname(UP_LASTNAME);

        mockMvc.perform(put("/vets/" + id)
                        .content(om.writeValueAsString(upVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/vets/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstname", is(UP_FIRSTNAME)))
                .andExpect(jsonPath("$.lastname", is(UP_LASTNAME)));

        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }


}
