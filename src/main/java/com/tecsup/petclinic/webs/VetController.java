package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exception.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetController {
    String name = null;

    private VetService vetService;

    private VetMapper mapper;

    /**
     *  Change
     * @param vetService
     * @param mapper
     */
    public VetController(VetService vetService, VetMapper mapper){
        this.vetService = vetService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/vets")
    public ResponseEntity<List<VetDTO>> findAllVets(){
        List<Vet> vets = vetService.findAll();
        log.info("vets: " + vets);
        vets.forEach(vet -> log.info("Vet >> {} ", vet));

        List<VetDTO> vetDTOS = this.mapper.toVetDTOList(vets);
        log.info("vetDTOS: " + vetDTOS);
        vetDTOS.forEach(item -> log.info("VetDTO >> {} ", item));

        return ResponseEntity.ok().body(vetDTOS);
    }

    /**
     * Create pet
     *
     * @param vetDTO
     * @return
     */
    @PostMapping(value = "/vets")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<VetDTO> createVet(@RequestBody VetDTO vetDTO) {
        Vet newVet = this.mapper.toVet(vetDTO);
        VetDTO newVetDTO = this.mapper.toVetDTO(vetService.create(newVet));

        return ResponseEntity.status(HttpStatus.CREATED).body(newVetDTO);
    }

    /**
     * Find pet by id
     *
     * @param id
     * @return
     * @throws VetNotFoundException
     */
    @GetMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> findVetById(@PathVariable Integer id) {
        VetDTO vetDTO = null;

        try {
            Vet vet = vetService.findById(id);
            vetDTO = this.mapper.toVetDTO(vet);
        } catch (VetNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vetDTO);
    }

    /**
     * Update and create pet
     *
     * @param vetDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> updateVet(@PathVariable Integer id, @RequestBody VetDTO vetDTO) {
        VetDTO updateVetDTO = null;

        try {
            Vet updateVet = mapper.toVet(vetDTO);

            updateVet.setId(vetDTO.getId());
            updateVet.setFirstName(vetDTO.getFirstname());
            updateVet.setLastName(vetDTO.getLastname());

            vetService.update(updateVet);

            updateVetDTO = this.mapper.toVetDTO(updateVet);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateVetDTO);
    }

    /**
     * Delete pet by id
     *
     * @param id
     */
    @DeleteMapping(value = "/vets/{id}")
    ResponseEntity<String> deleteVet(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return ResponseEntity.ok().body("Delete ID :" + id);
        } catch (VetNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
