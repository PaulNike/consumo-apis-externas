package com.codigo.consumo_apis_externas.service.impl;

import com.codigo.consumo_apis_externas.clients.ClientReniec;
import com.codigo.consumo_apis_externas.constants.Constants;
import com.codigo.consumo_apis_externas.dao.PersonaRepository;
import com.codigo.consumo_apis_externas.entity.PersonaEntity;
import com.codigo.consumo_apis_externas.redis.RedisService;
import com.codigo.consumo_apis_externas.request.PersonaRequest;
import com.codigo.consumo_apis_externas.response.ResponseBase;
import com.codigo.consumo_apis_externas.response.ResponseReniec;
import com.codigo.consumo_apis_externas.service.PersonaService;
import com.codigo.consumo_apis_externas.util.Util;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {
    private final PersonaRepository personaRepository;
    private final ClientReniec clientReniec;
    private final RedisService redisService;

    @Value("${token.api}")
    private String tokenApi;
    @Override
    public ResponseBase crearPersona(PersonaRequest personaRequest) {
        PersonaEntity personaEntity = getEntity(personaRequest);
        if(personaEntity != null){
            personaRepository.save(personaEntity);
            return new ResponseBase(Constants.CODIGO_EXITO, Constants.MENSAJE_EXITO, Optional.of(personaEntity));
        }else {
            return new ResponseBase(Constants.CODIGO_ERROR, Constants.MENSAJE_ERROR,Optional.empty());
        }

    }

    @Override
    public ResponseBase getPersona(String numDoc) {
        String redisInfo = redisService.getFromRedis(Constants.REDIS_KEY_GUARDAR+numDoc);
        if(redisInfo!=null){
            PersonaEntity persona = Util.convertirDesdeString(redisInfo,PersonaEntity.class);
            return new ResponseBase(Constants.CODIGO_EXITO, Constants.MENSAJE_EXITO_DESDE_REDIS, Optional.of(persona));
        }else {
            PersonaEntity persona = personaRepository.findByNumDoc(numDoc);
            String dataParaRedis = Util.convertirAString(persona);
            redisService.saveInRedis(Constants.REDIS_KEY_GUARDAR+numDoc,dataParaRedis,2);
            return new ResponseBase(Constants.CODIGO_EXITO, Constants.MENSAJE_EXITO_DESDE_BD, Optional.of(persona));
        }

    }

    @Override
    public void deletePersona(String numDoc) {
        redisService.deleteKey(Constants.REDIS_KEY_GUARDAR+numDoc);
    }

    private ResponseReniec getExecutionReniec(String dni){
        String auth = "Bearer "+tokenApi;
        ResponseReniec reniec = clientReniec.getInfoReniec(dni,auth);
        return reniec;
    }
    private PersonaEntity getEntity(PersonaRequest personaRequest){
        PersonaEntity personaEntity = new PersonaEntity();
        //Ejecutando la consulta con el DNI que llego al metodo
        ResponseReniec responseReniec = getExecutionReniec(personaRequest.getDni());
        if(responseReniec != null){
            personaEntity.setNombres(responseReniec.getNombres());
            personaEntity.setApellidoPaterno(responseReniec.getApellidoPaterno());
            personaEntity.setApellidoMaterno(responseReniec.getApellidoMaterno());
            personaEntity.setNumDoc(responseReniec.getNumeroDocumento());
            personaEntity.setTipoDoc(responseReniec.getTipoDocumento());
            personaEntity.setUsuaCrea(Constants.AUDIT_ADMIN);
            personaEntity.setDateCrea(new Timestamp(System.currentTimeMillis()));
            return personaEntity;
        }else {
            return null;
        }

    }
}
