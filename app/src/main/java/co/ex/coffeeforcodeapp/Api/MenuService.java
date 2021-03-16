package co.ex.coffeeforcodeapp.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuService {
    @GET("popular")
    Call<ArrayList<DtoMenu>> getPopularProducts ();
}
