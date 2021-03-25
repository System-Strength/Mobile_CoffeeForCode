package co.ex.coffeeforcodeapp.Api.Products;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MenuService {
    @GET("{cd_prod}")
    Call<DtoMenuById> getProductByCd (@Path("cd_prod") int cd_prod);

    @GET("products")
    @SerializedName("Products")
    Call<ArrayList<DtoMenu>> getAllProducts();

    @DELETE("deleteItenCart/{email_user}/{cd_prod}")
    Call<DtoMenu> removeProd(@Path("email_user") String email_user, @Path("cd_prod") int cd_prod);
}
