package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Vet;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface VetRepository
    extends JpaRepository<Vet, Integer> {

    // Fetch vets by FirstName
    List<Vet> findByFirstName(String firstName);

    // Fetch vets by LastName
    List<Vet> findByLastName(String lastName);

    @Override
    List<Vet> findAll();
}
