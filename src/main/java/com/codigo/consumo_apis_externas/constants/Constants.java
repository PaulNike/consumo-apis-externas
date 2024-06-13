package com.codigo.consumo_apis_externas.constants;

public class Constants {

    public static final Integer CODIGO_EXITO=2000;
    public static final Integer CODIGO_ERROR=2005;

    public static final String MENSAJE_EXITO="Persona Insertada Correctamente";
    public static final String MENSAJE_EXITO_DESDE_BD="Persona Encontrada en BD";
    public static final String MENSAJE_EXITO_DESDE_REDIS="Persona Encontrada en REDIS";
    public static final String MENSAJE_EXITO_DELETE="Persona eliminada de REDIS";
    public static final String MENSAJE_ERROR="Ocurrio un Error en la transacción";

    public static final String AUDIT_ADMIN="PRODRIGUEZ";

    public static final String REDIS_KEY_GUARDAR="API:CONSUMO:EXTERNA:";
}
