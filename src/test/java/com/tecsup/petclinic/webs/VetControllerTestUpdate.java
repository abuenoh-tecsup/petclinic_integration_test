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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Eduardo Bullon
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTestUpdate {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testUpdateVet() throws Exception {


        String FIRSTNAME = "Eduardo";
        String LASTNAME = "Bullon";

        String UP_FIRSTNAME = "Fernando";
        String UP_LASTNAME = "Vera";

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
