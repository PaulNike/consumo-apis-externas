package com.codigo.consumo_apis_externas.retrofit;

import com.codigo.consumo_apis_externas.response.ResponseReniec;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ReniecService {
    @GET("/v2/reniec/dni")
    Call<ResponseReniec> getDatosPersona(@Header("Authorization") String token, @Query("numero") String numero);
}
