package co.ex.coffeeforcodeapp.Api.ShoppingCart;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingCartService {
    @POST("insert/{email_user}/{cd_prod}/{qt_prod}")
    Call<DtoShoppingCart> insertItem (@Path("emai_user") String email_user, @Path("cd_prod") int cd_prod, @Path("qt_prod") int qt_prod);
}
