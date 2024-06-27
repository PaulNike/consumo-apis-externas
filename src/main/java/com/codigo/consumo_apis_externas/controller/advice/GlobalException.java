package com.codigo.consumo_apis_externas.controller.advice;

import com.codigo.consumo_apis_externas.constants.Constants;
import com.codigo.consumo_apis_externas.controller.personalizada.PersonaException;
import com.codigo.consumo_apis_externas.response.ResponseBase;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
@Log4j2
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBase> manejandoExepciones(Exception ex){
        //Aqui manejo exclusivamente lo qeu sucede cuando capturo una exepcion general.
        log.error("Error manejado desde ******manejandoExepciones******* ");
        ResponseBase response = new ResponseBase(Constants.CODIGO_ERROR, "ERROR INTERNO DEL SERVIDOR: " + ex.getMessage(), Optional.empty());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseBase> manejandoNullPointer(NullPointerException ex){
        //Aqui manejo exclusivamente lo qeu sucede cuando capturo una exepcion general.
        log.error("Error manejado desde ******manejandoNullPointer******* ");
        ResponseBase response = new ResponseBase(Constants.CODIGO_ERROR, "ERROR HAY UN DATO NULOOOOO: " + ex.getMessage(), Optional.empty());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersonaException.class)
    public ResponseEntity<ResponseBase> manejandoPersonaException(PersonaException ex){
        //Aqui manejo exclusivamente lo qeu sucede cuando capturo una exepcion general.
        log.error("Error manejado desde ******manejandoPersonaException******* ");
        ResponseBase response = new ResponseBase(Constants.CODIGO_ERROR, "ERROR EN LA PERSONA" + ex.getMessage(), Optional.empty());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}
