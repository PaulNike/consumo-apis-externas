package com.codigo.consumo_apis_externas.controller;

import com.codigo.consumo_apis_externas.constants.Constants;
import com.codigo.consumo_apis_externas.controller.personalizada.PersonaException;
import com.codigo.consumo_apis_externas.response.ResponseBase;
import com.codigo.consumo_apis_externas.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/empresa")
public class EmpresaController {

    private final PersonaService personaService;

    public EmpresaController(PersonaService personaService) {
        this.personaService = personaService;
    }


    @GetMapping("/{numDOc}")
    public ResponseEntity<ResponseBase> getPersona(@PathVariable String numDOc){
        throw new PersonaException(" * * * * Persona con el Documento: "+ numDOc + " Ya existe");
    }

}
