package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VetMapper {

    VetMapper INSTANCE = Mappers.getMapper(VetMapper.class);

    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    Vet toVet(VetDTO dto);

    @Mapping(source = "firstName", target = "firstname")
    @Mapping(source = "lastName", target = "lastname")
    VetDTO toVetDTO(Vet vet);

    List<VetDTO> toVetDTOList(List<Vet> vetList);

    List<Vet> toVetList(List<VetDTO> vetDTOList);
}
