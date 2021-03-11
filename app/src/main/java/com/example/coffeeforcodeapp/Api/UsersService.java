package com.example.coffeeforcodeapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersService {

    @POST("register")
    Call<DtoUsers> registerNewUse(@Body DtoUsers users);

    @GET("login/{email}/{password}")
    Call<DtoUsers> loginUser(@Path("email") String email, @Path("password") String password);

    @GET("/info/{email}")
    Call<DtoUsers> infoUser(@Body DtoUsers users);
}
