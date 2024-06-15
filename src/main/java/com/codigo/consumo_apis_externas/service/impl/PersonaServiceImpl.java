package com.codigo.consumo_apis_externas.service.impl;

import com.codigo.consumo_apis_externas.clients.ClientReniec;
import com.codigo.consumo_apis_externas.constants.Constants;
import com.codigo.consumo_apis_externas.dao.PersonaRepository;
import com.codigo.consumo_apis_externas.entity.PersonaEntity;
import com.codigo.consumo_apis_externas.redis.RedisService;
import com.codigo.consumo_apis_externas.request.PersonaRequest;
import com.codigo.consumo_apis_externas.response.ResponseBase;
import com.codigo.consumo_apis_externas.response.ResponseReniec;
import com.codigo.consumo_apis_externas.retrofit.ReniecService;
import com.codigo.consumo_apis_externas.retrofit.client.ReniecCliente;
import com.codigo.consumo_apis_externas.service.PersonaService;
import com.codigo.consumo_apis_externas.util.Util;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {
    private final PersonaRepository personaRepository;
    private final ClientReniec clientReniec;
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    ReniecService apiService = ReniecCliente.getClient().create(ReniecService.class);

    @Value("${token.api}")
    private String tokenApi;
    @Override
    public ResponseBase crearPersona(PersonaRequest personaRequest) throws IOException {
        PersonaEntity personaEntity = getEntityRestTemplate(personaRequest);
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

    private PersonaEntity getEntityRetrofit(PersonaRequest personaRequest) throws IOException {
        //Instancia de la entidad que se va guardar
        PersonaEntity personaEntity = new PersonaEntity();
        //Parametrización del objeto retrofit que se va a ejecutar
        Call<ResponseReniec> call = apiService.getDatosPersona("Bearer "+tokenApi,personaRequest.getDni());
        //EJecucion de la consulta con retrofit
        Response<ResponseReniec> ejecutandoConsulta = call.execute();
        //Validación de exito y no null
        if(ejecutandoConsulta.isSuccessful() && ejecutandoConsulta.body() != null){
            //Pasando la respuesta del Response de Retrofit al objeto que entiendo ResponseReniec
            ResponseReniec responseReniec = ejecutandoConsulta.body();
            //Seteando datos a la entidad de la respuesta obtenida
            personaEntity.setNombres(responseReniec.getNombres());
            personaEntity.setApellidoPaterno(responseReniec.getApellidoPaterno());
            personaEntity.setApellidoMaterno(responseReniec.getApellidoMaterno());
            personaEntity.setNumDoc(responseReniec.getNumeroDocumento());
            personaEntity.setTipoDoc(responseReniec.getTipoDocumento());
            personaEntity.setUsuaCrea(Constants.AUDIT_ADMIN);
            personaEntity.setDateCrea(new Timestamp(System.currentTimeMillis()));
            return personaEntity;
        }else{
            return null;
        }
    }

    private PersonaEntity getEntityRestTemplate(PersonaRequest personaRequest){
        String url = "https://api.apis.net.pe/v2/reniec/dni?numero="+personaRequest.getDni();

        try {
            ResponseEntity<ResponseReniec> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders(tokenApi)),
                    ResponseReniec.class
            );
            ResponseReniec responseReniec = response.getBody();
            PersonaEntity personaEntity = new PersonaEntity();

            personaEntity.setNombres(responseReniec.getNombres());
            personaEntity.setApellidoPaterno(responseReniec.getApellidoPaterno());
            personaEntity.setApellidoMaterno(responseReniec.getApellidoMaterno());
            personaEntity.setNumDoc(responseReniec.getNumeroDocumento());
            personaEntity.setTipoDoc(responseReniec.getTipoDocumento());
            personaEntity.setUsuaCrea(Constants.AUDIT_ADMIN);
            personaEntity.setDateCrea(new Timestamp(System.currentTimeMillis()));
            return personaEntity;
        }catch (HttpClientErrorException e){
            System.err.println("ERROR AL CONSUMIR EL API EXTERNA " +e.getStatusCode());
        }

        return null;
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + token);
        return headers;
    }
    //GSON
    //ASINCRONO= NO ENTREGA RESPUESTA INMEDIATA  | SINCRONO = Esperando un respuesta
    //
}
