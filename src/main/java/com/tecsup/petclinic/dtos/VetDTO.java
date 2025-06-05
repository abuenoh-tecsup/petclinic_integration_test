package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VetDTO {

    private Integer id;

    private String firstname;

    private String lastname;
}
