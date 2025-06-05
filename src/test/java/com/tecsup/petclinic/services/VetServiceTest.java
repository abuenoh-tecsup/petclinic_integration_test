package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class VetServiceTest {
    @Autowired
    private VetService vetService ;

    @Test
    public void testFindVetById() {
        String FIRST_NAME_EXPECTED = "James";

        Integer ID = 1;

        Vet vet = null;

        try {
            vet = this.vetService.findById(ID);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }
        assertEquals(FIRST_NAME_EXPECTED, vet.getFirstName());
    }

    @Test
    public void testFindVetByFirstName() {
        String FIND_FIRST_NAME = "James";

        int SIZE_EXPECTED = 1;

        List<Vet> vets = this.vetService.findByFirstName(FIND_FIRST_NAME);

        assertEquals(SIZE_EXPECTED, vets.size());
    }

    @Test
    public void testFindVetByLastName() {
        String FIND_LAST_NAME = "Carter";

        int SIZE_EXPECTED = 1;

        List<Vet> vets = this.vetService.findByLastName(FIND_LAST_NAME);

        assertEquals(SIZE_EXPECTED, vets.size());
    }

    @Test
    public void testCreateVet() {

        String FIRST_NAME = "Jaime";
        String LAST_NAME = "Gomez";

        Vet vet = new Vet(FIRST_NAME, LAST_NAME);

        Vet vetCreated = vetService.create(vet);

        log.info(">>> VET CREATED: " + vetCreated);

        assertNotNull(vetCreated.getId(), "El ID no debe ser nulo después de guardar");
        assertEquals(FIRST_NAME, vetCreated.getFirstName(), "El nombre no coincide");
        assertEquals(LAST_NAME, vetCreated.getLastName(), "El apellido no coincide");

    }

    @Test
    public void testUpdateVet() {

        String VET_FIRST_NAME = "Rafael";
        String VET_LAST_NAME = "Ortega";

        String UPDATED_VET_FIRST_NAME = "Eduardo";
        String UPDATED_VET_LAST_NAME = "Bullon";

        Vet vet = new Vet(VET_FIRST_NAME, VET_LAST_NAME);

        log.info(">" + vet);
        Vet vetCreated = this.vetService.create(vet);
        log.info(">>" + vetCreated);

        vetCreated.setFirstName(UPDATED_VET_FIRST_NAME);
        vetCreated.setLastName(UPDATED_VET_LAST_NAME);

        Vet updatedVet = this.vetService.update(vetCreated);
        log.info(">>>>" + updatedVet);

        assertEquals(UPDATED_VET_FIRST_NAME, updatedVet.getFirstName());
        assertEquals(UPDATED_VET_LAST_NAME, updatedVet.getLastName());
    }

    @Test
    public void testDeleteVet() {

        String FIRST_NAME = "Jaime";
        String LAST_NAME = "Gomez";

        // ------------ Create ---------------
        Vet vet = new Vet(FIRST_NAME, LAST_NAME);
        vet = this.vetService.create(vet);
        log.info("Vet creado: " + vet);

        // ------------ Delete ---------------
        try {
            this.vetService.delete(vet.getId());
        } catch (VetNotFoundException e) {
            fail("No se pudo eliminar el veterinario: " + e.getMessage());
        }

        // ------------ Validation ---------------
        try {
            this.vetService.findById(vet.getId());
            // Si encuentra el vet, la prueba debe fallar
            assertTrue(false, "El veterinario no debería existir después de eliminarlo");
        } catch (VetNotFoundException e) {
            // Esperado: el vet no debe encontrarse
            assertTrue(true);
        }
    }
	
}
