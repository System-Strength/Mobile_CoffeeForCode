package com.example.coffeeforcodeapp.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MenuService {
    @GET("popular")
    Call<ArrayList<DtoMenu>> getPopularProducts ();
}
