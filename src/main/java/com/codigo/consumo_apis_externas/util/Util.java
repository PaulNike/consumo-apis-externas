package com.codigo.consumo_apis_externas.util;

import com.codigo.consumo_apis_externas.entity.PersonaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

    public static String convertirAString(PersonaEntity persona){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(persona);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertirDesdeString(String json, Class<T> tipoClase){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json,tipoClase);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
