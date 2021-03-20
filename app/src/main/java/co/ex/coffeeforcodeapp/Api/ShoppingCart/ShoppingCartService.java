package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingCartService {

    @POST("insert/{email_user}/{cd_prod}/{qt_prod}")
    Call<DtoShoppingCart> insertItem (@Path("email_user") String email_user, @Path("cd_prod") int cd_prod, @Path("qt_prod") int qt_prod);

    @GET("{email_user}")
    Call<DtoShoppingCart> getCartInfomration (@Path("email_user") String email_user);
}
