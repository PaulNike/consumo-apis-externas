package com.codigo.consumo_apis_externas.service;

import com.codigo.consumo_apis_externas.request.PersonaRequest;
import com.codigo.consumo_apis_externas.response.ResponseBase;

import java.io.IOException;

public interface PersonaService {
    ResponseBase crearPersona(PersonaRequest personaRequest) throws IOException;
    ResponseBase getPersona(String numDoc);
    void deletePersona(String numDoc);
}
