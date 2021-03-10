package com.example.coffeeforcodeapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UsersService {

    @POST("register")
    public Call<DtoUsers> registerNewUse(@Body DtoUsers users);

    @POST("login")
    public Call<DtoUsers> loginUser(@Body DtoUsers users);

    @GET("/info/{email}")
    public Call<DtoUsers> infoUser(@Body DtoUsers users);
}
