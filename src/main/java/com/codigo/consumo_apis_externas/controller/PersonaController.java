package com.codigo.consumo_apis_externas.controller;

import com.codigo.consumo_apis_externas.constants.Constants;
import com.codigo.consumo_apis_externas.request.PersonaRequest;
import com.codigo.consumo_apis_externas.response.ResponseBase;
import com.codigo.consumo_apis_externas.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/persona")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<ResponseBase> crearPersona(@RequestBody PersonaRequest personaRequest){
        ResponseBase responseBase = personaService.crearPersona(personaRequest);
        if(responseBase.getCode() == Constants.CODIGO_EXITO){
            return ResponseEntity.ok(responseBase);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBase);
        }
    }

    @GetMapping("/{numDOc}")
    public ResponseEntity<ResponseBase> getPersona(@PathVariable String numDOc){
        ResponseBase responseBase = personaService.getPersona(numDOc);
        if(responseBase.getCode() == Constants.CODIGO_EXITO){
            return ResponseEntity.ok(responseBase);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBase);
        }
    }

    @DeleteMapping("/{numDOc}")
    public ResponseEntity<ResponseBase> deletePersona(@PathVariable String numDOc){
        personaService.deletePersona(numDOc);
        return ResponseEntity.ok(new ResponseBase(Constants.CODIGO_EXITO,Constants.MENSAJE_EXITO_DELETE, Optional.empty()));
    }


}
