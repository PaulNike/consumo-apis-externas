package com.codigo.consumo_apis_externas.service;

import com.codigo.consumo_apis_externas.request.PersonaRequest;
import com.codigo.consumo_apis_externas.response.ResponseBase;

public interface PersonaService {
    ResponseBase crearPersona(PersonaRequest personaRequest);
    ResponseBase getPersona(String numDoc);
    void deletePersona(String numDoc);
}
